package ru.practicum.explorewithmeservice.comment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmeservice.comment.dto.*;
import ru.practicum.explorewithmeservice.comment.model.CommentStatus;
import ru.practicum.explorewithmeservice.comment.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Calendar;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CommentController {

    private static final String fromRequestParam = "from";

    private static final String sizeRequestParam = "size";

    private static final String userIdAlias = "userId";

    private static final String eventIdAlias = "eventId";

    private static final String commentIdAlias = "commentId";

    private final CommentService commentService;

    @PostMapping(value = "/users/{" + userIdAlias + "}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponseDto createCommentByUser(
            @PathVariable(name = userIdAlias) Long userId,
            @RequestParam(name = eventIdAlias) Long eventId,
            @Valid @RequestBody CommentRequestDto request
    ) {
        return commentService.createCommentByUser(
                userId,
                eventId,
                request
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/events/{" + eventIdAlias + "}/comments")
    public CommentResponseUpdateDto updateCommentByUserByEvent(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = eventIdAlias) Long eventId,
            @Valid @RequestBody CommentRequestUpdateDto request
    ) {
        return commentService.updateCommentByUserByEvent(
                userId,
                eventId,
                request
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/comments/{" + commentIdAlias + "}/moderation")
    public CommentResponseDto updateCommentByUserByEventToModerate(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = commentIdAlias) Long commentId
    ) {
        return commentService.updateCommentByUserByEventToModerate(
                userId,
                commentId
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/comments/{" + commentIdAlias + "}/publish")
    public CommentResponseDto updateCommentByUserByEventToPublish(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = commentIdAlias) Long commentId
    ) {
        return commentService.updateCommentByUserByEventToPublish(
                userId,
                commentId
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/comments/{" + commentIdAlias + "}/draft")
    public CommentResponseDto updateCommentByUserByEventToDraft(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = commentIdAlias) Long commentId
    ) {
        return commentService.updateCommentByUserByEventToDraft(
                userId,
                commentId
        );
    }

    @GetMapping("/users/{" + userIdAlias + "}/events/{" + eventIdAlias + "}/comments")
    public List<CommentResponseDto> getCommentsByUserByEvent(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = eventIdAlias) Long eventId,
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size
    ) {
        return commentService.getCommentByUserByEvent(
                userId,
                eventId,
                from,
                size
        );
    }

    @GetMapping("/users/{" + userIdAlias + "}/comments")
    public List<CommentResponseDto> getCommentsByUser(
            @PathVariable(name = userIdAlias) Long userId,
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size
    ) {
        return commentService.getCommentsByUser(
                userId,
                from,
                size
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/comments/{" + commentIdAlias + "}")
    public CommentResponseDto updateCommentByUser(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = commentIdAlias) Long commentId,
            @Valid @RequestBody CommentRequestAdminDto request
    ) {
        return commentService.updateCommentByUser(
                userId,
                commentId,
                request
        );
    }

    @GetMapping("/comments")
    public List<CommentResponseDto> getCommentsByFilter(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "events", required = false) List<Long> events,
            @RequestParam(name = "rangeCreateStart", required = false) Calendar rangeCreateStart,
            @RequestParam(name = "rangeCreateEnd", required = false) Calendar rangeCreateEnd,
            @RequestParam(name = "rangeUpdateStart", required = false) Calendar rangeUpdateStart,
            @RequestParam(name = "rangeUpdateEnd", required = false) Calendar rangeUpdateEnd,
            @RequestParam(name = "onlyNotReply", required = false) Boolean onlyNotReply,
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size
    ) {
        return commentService.getCommentsByFilter(
                text,
                users,
                events,
                rangeCreateStart,
                rangeCreateEnd,
                rangeUpdateStart,
                rangeUpdateEnd,
                onlyNotReply,
                CommentStatus.PUBLISHED.toString(),
                from,
                size
        );
    }

    @GetMapping("/comments/{" + commentIdAlias + "}")
    public CommentResponseDto getComment(
            @PathVariable(name = commentIdAlias) Long commentId
    ) {
        return commentService.getComment(
                commentId
        );
    }

    @GetMapping("/events/{" + eventIdAlias + "}/comments")
    public List<CommentResponseDto> getCommentByEvent(
            @PathVariable(name = eventIdAlias) Long eventId,
            @RequestParam(name = "status", required = false) String status,
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size
    ) {
        return commentService.getCommentByEvent(
                eventId,
                status,
                from,
                size
        );
    }

    @PatchMapping("/admin/comments/{" + commentIdAlias + "}")
    public CommentResponseDto updateCommentByAdmin(
            @PathVariable(name = commentIdAlias) Long commentId,
            @Valid @RequestBody CommentRequestAdminDto request
    ) {
        return commentService.updateCommentByAdmin(
                commentId,
                request
        );
    }

    @GetMapping("/admin/comments")
    public List<CommentResponseDto> getCommentsByFilterByAdmin(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "events", required = false) List<Long> events,
            @RequestParam(name = "rangeCreateStart", required = false) Calendar rangeCreateStart,
            @RequestParam(name = "rangeCreateEnd", required = false) Calendar rangeCreateEnd,
            @RequestParam(name = "rangeUpdateStart", required = false) Calendar rangeUpdateStart,
            @RequestParam(name = "rangeUpdateEnd", required = false) Calendar rangeUpdateEnd,
            @RequestParam(name = "onlyNotReply", required = false) Boolean onlyNotReply,
            @RequestParam(name = "status", required = false) String status,
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size
    ) {
        return commentService.getCommentsByFilter(
                text,
                users,
                events,
                rangeCreateStart,
                rangeCreateEnd,
                rangeUpdateStart,
                rangeUpdateEnd,
                onlyNotReply,
                status,
                from,
                size
        );
    }

    @DeleteMapping("/comments/{" + commentIdAlias + "}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(
            @PathVariable(name = commentIdAlias) Long commentId
    ) {
        commentService.deleteComment(
                commentId
        );
    }
}
