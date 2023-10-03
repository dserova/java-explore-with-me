package ru.practicum.explorewithmeservice.request.service;

import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestRequestUpdateDto;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestResponseDto;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestResponseUpdateDto;

import java.util.List;

public interface ParticipationRequestService {

    List<ParticipationRequestResponseDto> getRequestsByUserByEvent(
            Long userId,
            Long eventId
    );

    ParticipationRequestResponseUpdateDto updateRequestsByUserByEvent(
            Long userId,
            Long eventId,
            ParticipationRequestRequestUpdateDto request
    );

    List<ParticipationRequestResponseDto> getRequestsPrivateByUser(
            Long userId
    );

    ParticipationRequestResponseDto createRequestsPrivateByUser(
            Long userId,
            Long eventId
    );

    ParticipationRequestResponseDto updateRequestsPrivateByUserByEventCancel(
            Long userId,
            Long requestsId
    );

}