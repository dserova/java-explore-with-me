package ru.practicum.explorewithmeservice.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithmeservice.user.model.User;

import java.util.Calendar;
import java.util.List;

@Data
@NoArgsConstructor
public class CommentResponseShortDto {
    private long id;
    private String text;
    private User author;
    private Calendar created;
    private Calendar updated;
    private List<CommentResponseShortDto> replies;
}