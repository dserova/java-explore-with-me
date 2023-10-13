package ru.practicum.explorewithmeservice.comment.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@NoArgsConstructor
public class CommentRequestAdminDto {
    private String text;
    private Long event;
    private Long author;
    private Calendar created;
    private Calendar updated;
    private String status;
}