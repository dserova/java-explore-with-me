package ru.practicum.explorewithmeservice.event.service;

import ru.practicum.explorewithmeservice.event.dto.EventRequestDto;
import ru.practicum.explorewithmeservice.event.dto.EventRequestUpdateDto;
import ru.practicum.explorewithmeservice.event.dto.EventResponseDto;
import ru.practicum.explorewithmeservice.event.model.EventState;

import java.util.Calendar;
import java.util.List;

public interface EventService {

    List<EventResponseDto> getAllByUser(
            int from,
            int size,
            Long userId
    );

    EventResponseDto createByUser(
            Long userId,
            EventRequestDto request
    );

    EventResponseDto getByUserByEvent(
            Long userId,
            Long eventId
    );

    EventResponseDto updateByUserByEvent(
            Long userId,
            Long eventId,
            EventRequestUpdateDto request
    );

    List<EventResponseDto> getByAdmin(
            List<Long> users,
            List<EventState> states,
            List<Long> categories,
            Calendar rangeStart,
            Calendar rangeEnd,
            int from,
            int size,
            String address
    );

    EventResponseDto updateByAdmin(
            Long eventId,
            EventRequestUpdateDto request
    );

    List<EventResponseDto> getPublicByFilter(
            String text,
            List<Long> categories,
            Boolean paid,
            Calendar rangeStart,
            Calendar rangeEnd,
            Boolean onlyAvailable,
            String sortState,
            int from,
            int size,
            String ip,
            String url,
            String app,
            String address
    );

    EventResponseDto getPublic(
            Long eventId,
            String ip,
            String url,
            String app,
            String address
    );
}