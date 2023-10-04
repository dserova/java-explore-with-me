package ru.practicum.explorewithmeservice.request.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithmeservice.request.model.EventRequestState;

import java.util.List;

@Data
@NoArgsConstructor
public class ParticipationRequestRequestUpdateDto {
    private List<Long> requestIds;
    private EventRequestState status;
}
