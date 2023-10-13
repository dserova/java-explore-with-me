package ru.practicum.explorewithmeservice.event.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmeservice.category.model.Category;
import ru.practicum.explorewithmeservice.category.repository.CategoryRepository;
import ru.practicum.explorewithmeservice.comment.dto.CommentResponseShortDto;
import ru.practicum.explorewithmeservice.comment.model.Comment;
import ru.practicum.explorewithmeservice.comment.model.CommentStatus;
import ru.practicum.explorewithmeservice.comment.repository.CommentRepository;
import ru.practicum.explorewithmeservice.comment.service.CommentService;
import ru.practicum.explorewithmeservice.error.*;
import ru.practicum.explorewithmeservice.event.dto.EventRequestDto;
import ru.practicum.explorewithmeservice.event.dto.EventRequestUpdateDto;
import ru.practicum.explorewithmeservice.event.dto.EventResponseDto;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.event.model.EventState;
import ru.practicum.explorewithmeservice.event.model.EventStateUpdate;
import ru.practicum.explorewithmeservice.event.model.SortState;
import ru.practicum.explorewithmeservice.event.repository.EventRepository;
import ru.practicum.explorewithmeservice.helpers.Helper;
import ru.practicum.explorewithmeservice.helpers.Paging;
import ru.practicum.explorewithmeservice.user.model.User;
import ru.practicum.explorewithmeservice.user.repository.UserRepository;
import ru.practicum.explorewithmestatsclient.service.ClientCreateHit;
import ru.practicum.explorewithmestatsclient.service.ClientGetStats;
import ru.practicum.explorewithmestatscommon.dto.EndpointHitDto;
import ru.practicum.explorewithmestatscommon.dto.ViewStatsDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    final RestTemplateBuilder builder = new RestTemplateBuilder();

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final CategoryRepository categoryRepository;

    private final CommentRepository commentRepository;

    private final CommentService commentService;

    private final Paging paging = new Paging();

    private final Helper helper = new Helper();

    @Override
    public List<EventResponseDto> getAllByUser(
            int start,
            int size,
            Long userId
    ) {
        Function<Page<Event>, Page<Event>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(EventResponseDto.class)
        ).apply(
                eventRepository.findAllByInitiator_id(userId, paging.getPageable(start, size))
        );
    }

    @Override
    public EventResponseDto createByUser(
            Long userId,
            EventRequestDto data
    ) {
        Function<EventRequestDto, EventRequestDto> chain = Function.identity();
        return chain.andThen(
                helper.to(Event.class)
        ).andThen(
                event -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(UserNotFoundException::new);
                    event.setInitiator(user);

                    Category cat = categoryRepository.findById(data.getCategory())
                            .orElseThrow(CategoryNotFoundException::new);
                    event.setCategory(cat);

                    event.setState(EventState.PENDING);

                    event.setCreatedOn(Calendar.getInstance());

                    Calendar needDate = Calendar.getInstance();
                    needDate.add(Calendar.HOUR, 2);

                    if (needDate.after(data.getEventDate())) {
                        throw new EventConflictException();
                    }
                    return event;
                }
        ).andThen(
                eventRepository::save
        ).andThen(
                helper.to(EventResponseDto.class)
        ).apply(
                data
        );
    }

    @Override
    public EventResponseDto getByUserByEvent(
            Long userId,
            Long eventId
    ) {
        Function<Event, Event> chain = Function.identity();
        return chain.andThen(
                helper.to(EventResponseDto.class)
        ).andThen(
                dto -> {

                    List<Long> events = new ArrayList<>();
                    events.add(eventId);

                    Page<Comment> comments = commentRepository.getComments(
                            null,
                            null,
                            events,
                            null,
                            null,
                            null,
                            null,
                            true,
                            CommentStatus.PUBLISHED,
                            paging.getPageable(0, 1000)
                    );

                    List<Comment> filteredComments = comments.stream().peek(
                            m -> commentService.onlyPublish(m, CommentStatus.PUBLISHED)
                    ).collect(Collectors.toList());

                    dto.setComments(
                            filteredComments.stream().map(
                                    helper.to(CommentResponseShortDto.class)
                            ).collect(Collectors.toList())
                    );

                    return dto;
                }
        ).apply(
                eventRepository.findByIdAndInitiator_id(eventId, userId)
                        .orElseThrow(() -> new EventNotFoundException("Check your parameters in the path."))
        );
    }

    // +++
    @Override
    public EventResponseDto updateByUserByEvent(
            Long userId,
            Long eventId,
            EventRequestUpdateDto data
    ) {
        Function<Event, Event> chain = Function.identity();
        return chain.andThen(
                updating -> {

                    Calendar needDate = Calendar.getInstance();
                    needDate.add(Calendar.HOUR, 2);

                    if (needDate.after(data.getEventDate())) {
                        throw new EventConflictException();
                    }

                    if (updating.getState() == EventState.PUBLISHED) {
                        throw new EventConflictException();
                    }

                    checkCategory(data, updating);

                    EventStateUpdate action = EventStateUpdate.from(data.getStateAction()).orElse(EventStateUpdate.PENDING_EVENT);

                    if (action.equals(EventStateUpdate.PENDING_EVENT)) {
                        updating.setState(EventState.PENDING);
                    }
                    if (action.equals(EventStateUpdate.PUBLISH_EVENT)) {
                        updating.setPublishedOn(Calendar.getInstance());
                        updating.setState(EventState.PUBLISHED);
                    }
                    if (action == EventStateUpdate.REJECT_EVENT || action == EventStateUpdate.CANCEL_REVIEW) {
                        updating.setState(EventState.CANCELED);
                    }

                    return updating;
                }
        ).andThen(
                eventRepository::save
        ).andThen(
                helper.to(EventResponseDto.class)
        ).apply(
                eventRepository.findByIdAndInitiator_id(eventId, userId)
                        .orElseThrow(() -> new EventNotFoundException("Check your parameters in the path."))
        );
    }

    private void checkCategory(EventRequestUpdateDto data, Event updating) {
        if (data.getCategory() != null) {
            updating.setCategory(
                    categoryRepository.findById(data.getCategory())
                            .orElseThrow(CategoryNotFoundException::new)
            );
        }

        Optional.ofNullable(data.getAnnotation()).ifPresent(updating::setAnnotation);
        Optional.ofNullable(data.getTitle()).ifPresent(updating::setTitle);
        Optional.ofNullable(data.getDescription()).ifPresent(updating::setDescription);
        Optional.ofNullable(data.getEventDate()).ifPresent(updating::setEventDate);
        Optional.ofNullable(data.getPaid()).ifPresent(updating::setPaid);
        Optional.ofNullable(data.getParticipantLimit()).ifPresent(updating::setParticipantLimit);
    }

    @Override
    public List<EventResponseDto> getByAdmin(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            Calendar rangeStart,
            Calendar rangeEnd,
            int from,
            int size,
            String addressStatistic) {

        Function<Page<Event>, Page<Event>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(EventResponseDto.class)
        ).andThen(
                events -> events.stream().map(
                        event -> {
                            ClientGetStats clientGetStats = new ClientGetStats(addressStatistic, builder);
                            List<String> uris = new ArrayList<>();
                            uris.add("/event/" + event.getId());

                            return getEventResponseDto(event, clientGetStats, uris);
                        }
                ).collect(Collectors.toList())
        ).apply(
                eventRepository.searchAdmin(
                        users,
                        states,
                        categories,
                        rangeStart,
                        rangeEnd,
                        paging.getPageable(from, size)
                )
        );
    }

    @NonNull
    private EventResponseDto getEventResponseDto(EventResponseDto event, ClientGetStats clientGetStats, List<String> uris) {
        Calendar begin = Calendar.getInstance();
        begin.add(Calendar.YEAR, -1);

        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 1);

        List<ViewStatsDto> list = clientGetStats.getStats(begin, end, uris, true);

        if (!list.isEmpty()) {
            event.setViews(list.get(0).getHits());
        } else {
            event.setViews(0L);
        }
        return event;
    }


    @Override
    public EventResponseDto updateByAdmin(
            Long eventId,
            EventRequestUpdateDto data
    ) {
        Function<Event, Event> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    Calendar needDate = Calendar.getInstance();
                    needDate.add(Calendar.HOUR, 1);

                    if (needDate.after(data.getEventDate())) {
                        throw new EventConflictException();
                    }

                    if (updating.getState() == EventState.PUBLISHED) {
                        throw new EventConflictException();
                    }

                    EventStateUpdate action = EventStateUpdate.from(data.getStateAction()).orElse(EventStateUpdate.REJECT_EVENT);

                    if (action == EventStateUpdate.PUBLISH_EVENT && updating.getState() == EventState.CANCELED) {
                        throw new EventConflictException();
                    }
                    if (action == EventStateUpdate.PUBLISH_EVENT && updating.getState() == EventState.PENDING) {
                        updating.setState(EventState.PUBLISHED);
                    }

                    if (action == EventStateUpdate.REJECT_EVENT || action == EventStateUpdate.CANCEL_REVIEW) {
                        updating.setState(EventState.CANCELED);
                    }

                    checkCategory(data, updating);

                    return updating;
                }
        ).andThen(
                eventRepository::save
        ).andThen(
                helper.to(EventResponseDto.class)
        ).apply(
                eventRepository.findById(eventId)
                        .orElseThrow(() -> new EventNotFoundException("Check your parameters in the path."))
        );

    }

    @Override
    public List<EventResponseDto> getPublicByFilter(
            String text,
            List<Long> categories,
            Boolean paid,
            Calendar rangeStart,
            Calendar rangeEnd,
            Boolean onlyAvailable,
            String sort,
            int from,
            int size,
            String ip,
            String url,
            String app,
            String addressStatistic
    ) {

        SortState sortState = SortState.from(sort).orElse(null);

        if (rangeStart != null && rangeEnd != null) {
            if (rangeStart.after(rangeEnd)) {
                throw new EventBadRequestException();
            }
        }

        if (rangeStart == null && rangeEnd == null) {
            rangeStart = Calendar.getInstance();
        }

        Calendar finalRangeStart = rangeStart;

        Function<Page<Event>, Page<Event>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(EventResponseDto.class)
        ).andThen(
                events -> {
                    // Thread problem in github, normal in local
                    //Thread thread = new Thread(() -> {
                    sendStatistic(ip, url, app, addressStatistic);
                    //});
                    //thread.start();
                    return events.stream().map(
                            event -> {

                                ClientGetStats clientGetStats = new ClientGetStats(addressStatistic, builder);

                                List<String> uris = new ArrayList<>();
                                uris.add(url);

                                return getEventResponseDto(event, clientGetStats, uris);
                            }
                    ).collect(Collectors.toList());
                }
        ).apply(
                eventRepository.search(
                        text,
                        categories,
                        paid,
                        finalRangeStart,
                        rangeEnd,
                        String.valueOf(sortState),
                        onlyAvailable,
                        paging.getPageable(from, size)
                )
        );
    }

    private void sendStatistic(String ip, String url, String app, String addressStatistic) {
        EndpointHitDto statistic = new EndpointHitDto();
        statistic.setApp(app);
        statistic.setIp(ip);
        statistic.setUri(url);
        statistic.setTimestamp(Calendar.getInstance());
        ClientCreateHit clientCreateHit = new ClientCreateHit(addressStatistic, builder);
        clientCreateHit.createHit(statistic);
    }

    @Override
    public EventResponseDto getPublic(
            Long eventId,
            String ip,
            String url,
            String app,
            String addressStatistic
    ) {
        Function<Event, Event> chain = Function.identity();
        return chain.andThen(
                helper.to(EventResponseDto.class)
        ).andThen(
                event -> {
                    // Thread problem in github, normal in local
                    //Thread thread = new Thread(() -> {
                    sendStatistic(ip, url, app, addressStatistic);
                    //});
                    //thread.start();

                    ClientGetStats clientGetStats = new ClientGetStats(addressStatistic, builder);

                    List<String> uris = new ArrayList<>();
                    uris.add(url);

                    return getEventResponseDto(event, clientGetStats, uris);
                }
        ).andThen(
                dto -> {
                    List<Long> events = new ArrayList<>();
                    events.add(eventId);
                    Page<Comment> comments = commentRepository.getComments(
                            null,
                            null,
                            events,
                            null,
                            null,
                            null,
                            null,
                            true,
                            CommentStatus.PUBLISHED,
                            paging.getPageable(0, 1000)
                    );

                    List<Comment> filteredComments = comments.stream().peek(
                            m -> commentService.onlyPublish(m, CommentStatus.PUBLISHED)
                    ).collect(Collectors.toList());

                    dto.setComments(
                            filteredComments.stream().map(
                                    helper.to(CommentResponseShortDto.class)
                            ).collect(Collectors.toList())
                    );

                    return dto;
                }
        ).andThen(
                helper.to(EventResponseDto.class)
        ).apply(
                eventRepository.getPublic(eventId)
                        .orElseThrow(EventNotFoundException::new)
        );
    }
}


