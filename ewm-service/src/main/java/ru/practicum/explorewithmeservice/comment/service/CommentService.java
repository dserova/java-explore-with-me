package ru.practicum.explorewithmeservice.comment.service;

import ru.practicum.explorewithmeservice.comment.dto.*;
import ru.practicum.explorewithmeservice.comment.model.Comment;
import ru.practicum.explorewithmeservice.comment.model.CommentStatus;

import java.util.Calendar;
import java.util.List;

public interface CommentService {

    CommentResponseDto createCommentByUser(
            Long userId,
            Long eventId,
            CommentRequestDto request
    );

    CommentResponseUpdateDto updateCommentByUserByEvent(
            Long userId,
            Long eventId,
            CommentRequestUpdateDto request
    );

    CommentResponseDto updateCommentByUserByEventToModerate(
            Long userId,
            Long commentId
    );

    CommentResponseDto updateCommentByUserByEventToPublish(
            Long userId,
            Long commentId
    );

    CommentResponseDto updateCommentByUserByEventToDraft(
            Long userId,
            Long commentId
    );

    List<CommentResponseDto> getCommentByUserByEvent(
            Long userId,
            Long eventId,
            int from,
            int size
    );

    List<CommentResponseDto> getCommentsByUser(
            Long userId,
            int from,
            int size
    );

    List<CommentResponseDto> getCommentsByFilter(
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
    );

    CommentResponseDto getComment(
            Long commentId
    );

    List<CommentResponseDto> getCommentByEvent(
            Long eventId,
            String status,
            int from,
            int size
    );

    CommentResponseDto updateCommentByAdmin(
            Long commentId,
            CommentRequestAdminDto request
    );

    void deleteComment(
            Long commentId
    );

    CommentResponseDto updateCommentByUser(
            Long userId,
            Long commentId,
            CommentRequestAdminDto request
    );

    void onlyPublish(Comment m, CommentStatus commentStatus);
}