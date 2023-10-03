package ru.practicum.explorewithmeservice.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParticipationRequestResponseUpdateDto {
    List<ParticipationRequestResponseDto> confirmedRequests;
    List<ParticipationRequestResponseDto> rejectedRequests;
}
