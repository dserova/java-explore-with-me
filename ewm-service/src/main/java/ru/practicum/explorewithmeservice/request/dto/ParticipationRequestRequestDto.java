package ru.practicum.explorewithmeservice.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;

@Data
@NoArgsConstructor
public class ParticipationRequestRequestDto {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private Calendar created;
    private Long event;
    private Long id;
    private Long requester;
    private String status;
}
