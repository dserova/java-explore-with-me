package ru.practicum.explorewithmeservice.comment.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.explorewithmeservice.comment.dto.*;
import ru.practicum.explorewithmeservice.comment.model.Comment;
import ru.practicum.explorewithmeservice.comment.model.CommentStatus;
import ru.practicum.explorewithmeservice.comment.repository.CommentRepository;
import ru.practicum.explorewithmeservice.error.CommentConflictException;
import ru.practicum.explorewithmeservice.error.CommentNotFoundException;
import ru.practicum.explorewithmeservice.error.EventNotFoundException;
import ru.practicum.explorewithmeservice.error.UserNotFoundException;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.event.model.EventState;
import ru.practicum.explorewithmeservice.event.repository.EventRepository;
import ru.practicum.explorewithmeservice.helpers.Helper;
import ru.practicum.explorewithmeservice.helpers.Paging;
import ru.practicum.explorewithmeservice.user.model.User;
import ru.practicum.explorewithmeservice.user.repository.UserRepository;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final EventRepository eventRepository;

    private final UserRepository userRepository;

    private final ModelMapper mapper;

    private final Paging paging = new Paging();

    private final Helper helper = new Helper();

    public void onlyPublish(
            Comment comment,
            CommentStatus status
    ) {
        Predicate<Comment> streamsPredicate = item -> item.getStatus().equals(status);
        List<Comment> filteredComments = comment.getReplies().stream().filter(streamsPredicate).collect(Collectors.toList());
        filteredComments.stream().peek(
                m -> onlyPublish(m, status)
        );
        comment.setReplies(filteredComments);
    }


    public Boolean checkReply(Long commentId) {
        AtomicReference<Boolean> pubTarget = new AtomicReference<>(false);
        commentRepository.findById(commentId).ifPresent(
                reply -> {
                    commentRepository.findByRepliesContains(reply).ifPresentOrElse(
                            replyTarget -> {
                                pubTarget.set(replyTarget.getStatus().equals(CommentStatus.PUBLISHED));
                            },
                            () -> {
                                pubTarget.set(true);
                            }
                    );
                }
        );
        return pubTarget.get();
    }


    @Override
    public void deleteComment(Long commentId) {
        commentRepository.findById(commentId).ifPresent(
                reply -> {
                    commentRepository.findByRepliesContains(reply).ifPresent(
                            replyTarget -> {
                                replyTarget.getReplies().remove(reply);
                            }
                    );
                }
        );
        commentRepository.deleteById(commentId);
    }

    @Override
    public CommentResponseDto createCommentByUser(
            Long userId,
            Long eventId,
            CommentRequestDto data
    ) {
        Function<CommentRequestDto, CommentRequestDto> chain = Function.identity();
        return chain.andThen(
                helper.to(Comment.class)
        ).andThen(
                creating -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(UserNotFoundException::new);
                    creating.setAuthor(user);
                    Event event = eventRepository.findById(eventId)
                            .orElseThrow(EventNotFoundException::new);
                    creating.setEvent(event);
                    if (
                            creating.getEvent().getRequestModeration()
                    ) {
                        creating.setStatus(CommentStatus.MODERATION);
                    } else {
                        creating.setStatus(CommentStatus.PUBLISHED);
                    }
                    if (
                            creating.getEvent().getState().equals(EventState.PENDING)
                    ) {
                        throw new CommentConflictException("event not published");
                    }
                    creating.setUpdated(Calendar.getInstance());
                    return creating;
                }
        ).andThen(
                commentRepository::save
        ).andThen(
                creating -> {
                    if (data.getReplyTarget() != null && data.getReplyTarget() != creating.getId()) {
                        commentRepository.findById(data.getReplyTarget())
                                .ifPresent(
                                        target -> {
                                            if (target.getReplies() == null) {
                                                target.setReplies(new ArrayList<>());
                                            }
                                            target.getReplies().add(creating);
                                        }
                                );
                    }
                    return creating;
                }
        ).andThen(
                helper.to(CommentResponseDto.class)
        ).apply(
                data
        );
    }

    @Override
    public CommentResponseUpdateDto updateCommentByUserByEvent(
            Long userId,
            Long eventId,
            CommentRequestUpdateDto data
    ) {
        Function<List<Comment>, List<Comment>> chain = Function.identity();

        CommentResponseUpdateDto commentResponseUpdateDto = new CommentResponseUpdateDto(new ArrayList<>(), new ArrayList<>());

        return chain.andThen(
                update -> {
                    update.stream().map(
                            c -> {
                                if (
                                        !c.getEvent().getState().equals(EventState.PENDING)
                                ) {
                                    if (
                                            !c.getEvent().getRequestModeration()
                                    ) {
                                        if (!checkReply(c.getId())) {
                                            throw new CommentConflictException("root not published");
                                        }
                                        c.setStatus(CommentStatus.PUBLISHED);
                                        commentRepository.save(c);
                                        commentResponseUpdateDto.getConfirmedComment().add(
                                                mapper.map(c, CommentResponseDto.class)
                                        );
                                        return commentResponseUpdateDto;
                                    }

                                    if (data.getStatus().equals(CommentStatus.PUBLISHED)) {

                                        if (!checkReply(c.getId())) {
                                            throw new CommentConflictException("root not published");
                                        }

                                        c.setStatus(data.getStatus());
                                        commentResponseUpdateDto.getConfirmedComment().add(
                                                mapper.map(c, CommentResponseDto.class)
                                        );
                                    } else {
                                        c.setStatus(CommentStatus.DRAFT);
                                        commentResponseUpdateDto.getRejectedComment().add(
                                                mapper.map(c, CommentResponseDto.class)
                                        );
                                    }
                                } else {
                                    throw new CommentConflictException("event not published");
                                }
                                return commentRepository.save(c);
                            }
                    ).collect(Collectors.toList());
                    return commentResponseUpdateDto;
                }

        ).apply(
                commentRepository.updateMulti(
                        userId,
                        eventId,
                        data.getCommentIds()
                )
        );
    }

    @Override
    public CommentResponseDto updateCommentByUserByEventToModerate(
            Long userId,
            Long commentId
    ) {
        Function<Comment, Comment> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    updating.setStatus(CommentStatus.MODERATION);
                    return updating;
                }
        ).andThen(
                commentRepository::save
        ).andThen(
                helper.to(CommentResponseDto.class)
        ).apply(
                commentRepository.findByIdAndAuthor_Id(
                                commentId,
                                userId
                        )
                        .orElseThrow(CommentNotFoundException::new)
        );
    }

    @Override
    public CommentResponseDto updateCommentByUserByEventToPublish(
            Long userId,
            Long commentId
    ) {
        Function<Comment, Comment> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    if (checkReply(commentId)) {
                        updating.setStatus(CommentStatus.PUBLISHED);
                    } else {
                        throw new CommentConflictException("root not published");
                    }
                    return updating;
                }
        ).andThen(
                commentRepository::save
        ).andThen(
                helper.to(CommentResponseDto.class)
        ).apply(
                commentRepository.findByIdAndAuthor_Id(
                                commentId,
                                userId
                        )
                        .orElseThrow(CommentNotFoundException::new)
        );
    }

    @Override
    public CommentResponseDto updateCommentByUserByEventToDraft(
            Long userId,
            Long commentId
    ) {
        Function<Comment, Comment> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    updating.setStatus(CommentStatus.DRAFT);
                    return updating;
                }
        ).andThen(
                commentRepository::save
        ).andThen(
                helper.to(CommentResponseDto.class)
        ).apply(
                commentRepository.findByIdAndAuthor_Id(
                                commentId,
                                userId
                        )
                        .orElseThrow(CommentNotFoundException::new)
        );
    }

    @Override
    public List<CommentResponseDto> getCommentByUserByEvent(
            Long userId,
            Long eventId,
            int from,
            int size
    ) {
        Function<Page<Comment>, Page<Comment>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(CommentResponseDto.class)
        ).apply(
                commentRepository.findAllByEvent_IdAndEvent_Initiator_Id(
                        eventId,
                        userId,
                        paging.getPageable(from, size)
                )
        );
    }

    @Override
    public List<CommentResponseDto> getCommentsByUser(
            Long userId,
            int from,
            int size
    ) {
        Function<Page<Comment>, Page<Comment>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(CommentResponseDto.class)
        ).apply(
                commentRepository.findAllByAuthor_Id(
                        userId,
                        paging.getPageable(from, size)
                )
        );
    }

    @Override
    public List<CommentResponseDto> getCommentsByFilter(
            String text,
            List<Long> users,
            List<Long> events,
            Calendar rangeCreateStart,
            Calendar rangeCreateEnd,
            Calendar rangeUpdateStart,
            Calendar rangeUpdateEnd,
            Boolean onlyNotReply,
            String status,
            int from,
            int size
    ) {
        CommentStatus resolvedStatus = CommentStatus.from(status).orElse(null);

        Function<Page<Comment>, Page<Comment>> chain = Function.identity();
        return chain.andThen(
                list -> {
                    if (resolvedStatus != null) {
                        return list.stream().map(
                                m -> {
                                    onlyPublish(m, resolvedStatus);
                                    return m;
                                }
                        ).collect(Collectors.toList());
                    } else {
                        return list.toList();
                    }
                }
        ).andThen(
                helper.fromList(CommentResponseDto.class)
        ).apply(
                commentRepository.getComments(
                        text,
                        users,
                        events,
                        rangeCreateStart,
                        rangeCreateEnd,
                        rangeUpdateStart,
                        rangeUpdateEnd,
                        onlyNotReply,
                        resolvedStatus,
                        paging.getPageable(from, size)
                )
        );
    }

    @Override
    public CommentResponseDto getComment(
            Long commentId
    ) {
        Function<Comment, Comment> chain = Function.identity();
        return chain.andThen(
                helper.to(CommentResponseDto.class)
        ).apply(
                commentRepository.findById(commentId)
                        .orElseThrow(CommentNotFoundException::new)
        );
    }

    @Override
    public List<CommentResponseDto> getCommentByEvent(
            Long eventId,
            String status,
            int from,
            int size
    ) {

        CommentStatus resolvedStatus = CommentStatus.from(status).orElse(null);

        if (resolvedStatus == CommentStatus.DRAFT) {
            throw new CommentConflictException("not permit");
        }

        Function<Page<Comment>, Page<Comment>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(CommentResponseDto.class)
        ).andThen(
                ret -> ret.stream().peek(
                        m -> m.setReplies(null)
                ).collect(Collectors.toList())
        ).apply(
                commentRepository.getAllByEventIdAndStatus(
                        eventId,
                        resolvedStatus,
                        paging.getPageable(from, size)
                )
        );
    }

    @Override
    public CommentResponseDto updateCommentByAdmin(
            Long commentId,
            CommentRequestAdminDto data
    ) {
        Function<Comment, Comment> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    Optional.ofNullable(data.getText()).ifPresent(updating::setText);
                    Optional.ofNullable(data.getUpdated()).ifPresent(updating::setUpdated);
                    Optional.ofNullable(data.getCreated()).ifPresent(updating::setCreated);
                    CommentStatus status = CommentStatus.from(data.getStatus())
                            .orElseThrow(UserNotFoundException::new);////

                    if (data.getStatus() != null) {
                        updating.setStatus(status);
                    }
                    if (data.getAuthor() != null) {
                        User user = userRepository.findById(data.getAuthor())
                                .orElseThrow(UserNotFoundException::new);
                        updating.setAuthor(user);
                    }
                    if (data.getEvent() != null) {
                        Event event = eventRepository.findById(data.getEvent())
                                .orElseThrow(EventNotFoundException::new);
                        updating.setEvent(event);
                    }
                    return updating;
                }
        ).andThen(
                commentRepository::save
        ).andThen(
                helper.to(CommentResponseDto.class)
        ).apply(
                commentRepository.findById(commentId)
                        .orElseThrow(CommentNotFoundException::new)
        );
    }

    @Override
    public CommentResponseDto updateCommentByUser(
            Long userId,
            Long commentId,
            CommentRequestAdminDto data
    ) {
        Function<Comment, Comment> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    if (Objects.equals(updating.getAuthor().getId(), userId)) {
                        Optional.ofNullable(data.getText()).ifPresent(updating::setText);
                    }
                    return updating;
                }
        ).andThen(
                commentRepository::save
        ).andThen(
                helper.to(CommentResponseDto.class)
        ).apply(
                commentRepository.findById(commentId)
                        .orElseThrow(CommentNotFoundException::new)
        );
    }
}


