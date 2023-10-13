package ru.practicum.explorewithmeservice.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class CommentRequestDto {
    @Length(min = 5, max = 5000, message = "Text is not valid")
    private String text;
    private String status;
    private Long replyTarget;
}