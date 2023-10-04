package ru.practicum.explorewithmeservice.request.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmeservice.error.EventConflictException;
import ru.practicum.explorewithmeservice.error.EventNotFoundException;
import ru.practicum.explorewithmeservice.error.UserNotFoundException;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.event.model.EventState;
import ru.practicum.explorewithmeservice.event.repository.EventRepository;
import ru.practicum.explorewithmeservice.helpers.Helper;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestRequestUpdateDto;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestResponseDto;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestResponseUpdateDto;
import ru.practicum.explorewithmeservice.request.model.EventRequestState;
import ru.practicum.explorewithmeservice.request.model.ParticipationRequest;
import ru.practicum.explorewithmeservice.request.repository.ParticipationRequestRepository;
import ru.practicum.explorewithmeservice.user.model.User;
import ru.practicum.explorewithmeservice.user.repository.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final ParticipationRequestRepository participationRequestRepository;

    private final ModelMapper mapper;

    private final Helper helper = new Helper();

    @Override
    public List<ParticipationRequestResponseDto> getRequestsByUserByEvent(
            Long userId,
            Long eventId
    ) {
        Function<List<ParticipationRequest>, List<ParticipationRequest>> chain = Function.identity();
        return chain.andThen(
                helper.fromList(ParticipationRequestResponseDto.class)
        ).apply(
                participationRequestRepository.findAllByEvent_IdAndEvent_Initiator_Id(eventId, userId)
        );
    }

    @Override
    public ParticipationRequestResponseUpdateDto updateRequestsByUserByEvent(
            Long userId,
            Long eventId,
            ParticipationRequestRequestUpdateDto data
    ) {
        Function<List<ParticipationRequest>, List<ParticipationRequest>> chain = Function.identity();

        ParticipationRequestResponseUpdateDto participationRequestResponseUpdateDto = new ParticipationRequestResponseUpdateDto(new ArrayList<>(), new ArrayList<>());

        return chain.andThen(
                update -> {
                    update.stream().map(
                            pr -> {
                                // no edit
                                if (
                                        !pr.getEvent().getRequestModeration() ||
                                                pr.getEvent().getParticipantLimit().equals(0)
                                ) {
                                    participationRequestResponseUpdateDto.getRejectedRequests().add(
                                            mapper.map(pr, ParticipationRequestResponseDto.class)
                                    );
                                    return pr;
                                }

                                Long participantCount = participationRequestRepository.countByEvent_IdAndStatus(eventId, EventRequestState.CONFIRMED);

                                if (
                                        pr.getEvent().getParticipantLimit() > participantCount &&
                                                pr.getStatus().equals(EventRequestState.PENDING)
                                ) {
                                    if (data.getStatus().equals(EventRequestState.CONFIRMED)) {
                                        pr.setStatus(data.getStatus());
                                        participationRequestResponseUpdateDto.getConfirmedRequests().add(
                                                mapper.map(pr, ParticipationRequestResponseDto.class)
                                        );
                                    } else {
                                        pr.setStatus(EventRequestState.REJECTED);
                                        participationRequestResponseUpdateDto.getRejectedRequests().add(
                                                mapper.map(pr, ParticipationRequestResponseDto.class)
                                        );
                                    }
                                }

                                if (
                                        pr.getEvent().getParticipantLimit() <= participantCount &&
                                                pr.getStatus().equals(EventRequestState.PENDING)
                                ) {
                                    throw new EventConflictException();
                                }

                                if (
                                        data.getStatus().equals(EventRequestState.REJECTED) &&
                                                pr.getStatus().equals(EventRequestState.CONFIRMED)
                                ) {
                                    throw new EventConflictException();
                                }
                                return participationRequestRepository.save(pr);
                            }
                    ).collect(Collectors.toList());
                    return participationRequestResponseUpdateDto;
                }
        ).apply(
                participationRequestRepository.findRequestForUpdate(
                        userId,
                        eventId,
                        data.getRequestIds()
                )
        );
    }

    @Override
    public List<ParticipationRequestResponseDto> getRequestsPrivateByUser(
            Long userId
    ) {
        Function<List<ParticipationRequest>, List<ParticipationRequest>> chain = Function.identity();
        return chain.andThen(
                helper.fromList(ParticipationRequestResponseDto.class)
        ).apply(
                participationRequestRepository.findAllByRequester_Id(userId)
        );
    }

    @Override
    public ParticipationRequestResponseDto createRequestsPrivateByUser(
            Long userId,
            Long eventId
    ) {
        Function<Optional<List<ParticipationRequest>>, Optional<List<ParticipationRequest>>> chain = Function.identity();
        return chain.andThen(
                list -> {
                    list.ifPresent(
                            pr -> {
                                if (!pr.isEmpty()) {
                                    throw new EventConflictException();
                                }
                            }
                    );

                    ParticipationRequest newParticipationRequest = new ParticipationRequest();
                    User user = userRepository.findById(userId)
                            .orElseThrow(UserNotFoundException::new);
                    newParticipationRequest.setRequester(user);

                    Event event = eventRepository.findById(eventId)
                            .orElseThrow(EventNotFoundException::new);
                    newParticipationRequest.setEvent(event);

                    newParticipationRequest.setStatus(EventRequestState.PENDING);

                    newParticipationRequest.setCreated(Calendar.getInstance());

                    if (newParticipationRequest.getRequester().equals(newParticipationRequest.getEvent().getInitiator())) {
                        throw new EventConflictException();
                    }
                    if (newParticipationRequest.getEvent().getState() != EventState.PUBLISHED) {
                        throw new EventConflictException();
                    }

                    Long participantCount = participationRequestRepository.countByEvent_IdAndStatus(eventId, EventRequestState.CONFIRMED);

                    if (
                            participantCount >= newParticipationRequest.getEvent().getParticipantLimit() &&
                                    newParticipationRequest.getEvent().getParticipantLimit() > 0
                    ) {
                        throw new EventConflictException();
                    }

                    if (
                            !newParticipationRequest.getEvent().getRequestModeration() ||
                                    newParticipationRequest.getEvent().getParticipantLimit().equals(0)
                    ) {
                        newParticipationRequest.setStatus(EventRequestState.CONFIRMED);
                    }
                    return newParticipationRequest;
                }
        ).andThen(
                participationRequestRepository::save
        ).andThen(
                helper.to(ParticipationRequestResponseDto.class)
        ).apply(
                participationRequestRepository.findAllByEvent_IdAndRequester_Id(eventId, userId)
        );
    }

    @Override
    public ParticipationRequestResponseDto updateRequestsPrivateByUserByEventCancel(Long userId, Long requestsId) {
        Function<ParticipationRequest, ParticipationRequest> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    updating.setStatus(EventRequestState.CANCELED);
                    return updating;
                }
        ).andThen(
                participationRequestRepository::save
        ).andThen(
                helper.to(ParticipationRequestResponseDto.class)
        ).apply(
                participationRequestRepository.findById(requestsId)
                        .orElseThrow(UserNotFoundException::new)
        );
    }
}


