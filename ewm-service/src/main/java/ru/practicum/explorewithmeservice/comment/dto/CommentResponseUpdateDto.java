package ru.practicum.explorewithmeservice.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentResponseUpdateDto {
    List<CommentResponseDto> confirmedComment;
    List<CommentResponseDto> rejectedComment;
}
