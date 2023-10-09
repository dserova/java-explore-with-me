package ru.practicum.explorewithmeservice.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithmeservice.comment.model.CommentStatus;

import java.util.List;

@Data
@NoArgsConstructor
public class CommentRequestUpdateDto {
    private List<Long> commentIds;
    private CommentStatus status;
}
