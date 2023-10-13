package ru.practicum.explorewithmeservice.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithmeservice.comment.model.Comment;
import ru.practicum.explorewithmeservice.comment.model.CommentStatus;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.user.model.User;

import java.util.Calendar;
import java.util.List;

@Data
@NoArgsConstructor
public class CommentResponseDto {
    private long id;
    private String text;
    private Event event;
    private User author;
    private Calendar created;
    private Calendar updated;
    private CommentStatus status;
    private List<Comment> replies;
}