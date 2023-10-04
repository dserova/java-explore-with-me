package ru.practicum.explorewithmeservice.event.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmeservice.event.dto.EventRequestDto;
import ru.practicum.explorewithmeservice.event.dto.EventRequestUpdateDto;
import ru.practicum.explorewithmeservice.event.dto.EventResponseDto;
import ru.practicum.explorewithmeservice.event.model.EventState;
import ru.practicum.explorewithmeservice.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Calendar;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class EventController {

    private static final String fromRequestParam = "from";
    private static final String sizeRequestParam = "size";
    private static final String userIdAlias = "userId";
    private static final String eventIdAlias = "eventId";
    private final EventService eventService;

    @GetMapping("/users/{" + userIdAlias + "}/events")
    public List<EventResponseDto> getAllByUser(
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size,
            @PathVariable(name = userIdAlias) Long userId
    ) {
        return eventService.getAllByUser(
                from,
                size,
                userId
        );
    }

    @PostMapping(value = "/users/{" + userIdAlias + "}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createByUser(
            @PathVariable(name = userIdAlias) Long userId,
            @Valid @RequestBody EventRequestDto request
    ) {
        return eventService.createByUser(
                userId,
                request
        );
    }

    @GetMapping("/users/{" + userIdAlias + "}/events/{" + eventIdAlias + "}")
    public EventResponseDto getByUserByEvent(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = eventIdAlias) Long eventId
    ) {
        return eventService.getByUserByEvent(
                userId,
                eventId
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/events/{" + eventIdAlias + "}")
    public EventResponseDto updateByUserByEvent(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = eventIdAlias) Long eventId,
            @Valid @RequestBody EventRequestUpdateDto request
    ) {
        return eventService.updateByUserByEvent(
                userId,
                eventId,
                request
        );
    }

    @GetMapping("/admin/events")
    public List<EventResponseDto> getByAdmin(
            @RequestParam(name = "users", required = false) List<Long> users,
            @RequestParam(name = "states", required = false) List<EventState> states,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "rangeStart", required = false) Calendar rangeStart,
            @RequestParam(name = "rangeEnd", required = false) Calendar rangeEnd,
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size,
            @Value("${ewm-stats-service.url}") String addressStatistic
    ) {
        return eventService.getByAdmin(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd,
                from,
                size,
                addressStatistic
        );
    }

    @PatchMapping("/admin/events/{" + eventIdAlias + "}")
    public EventResponseDto updateByAdmin(
            @PathVariable(name = eventIdAlias) Long eventId,
            @Valid @RequestBody EventRequestUpdateDto request
    ) {
        return eventService.updateByAdmin(
                eventId,
                request
        );
    }

    @GetMapping("/events")
    public List<EventResponseDto> getPublicByFilter(
            @RequestParam(name = "text", required = false) String text,
            @RequestParam(name = "categories", required = false) List<Long> categories,
            @RequestParam(name = "paid", required = false) Boolean paid,
            @RequestParam(name = "rangeStart", required = false) Calendar rangeStart,
            @RequestParam(name = "rangeEnd", required = false) Calendar rangeEnd,
            @RequestParam(name = "onlyAvailable", required = false) Boolean onlyAvailable,
            @RequestParam(name = "sort", required = false) String sortState,
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size,
            HttpServletRequest request,
            @Value("${application.name}") String appName,
            @Value("${ewm-stats-service.url}") String addressStatistic
    ) {
        return eventService.getPublicByFilter(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sortState,
                from,
                size,
                request.getRemoteAddr(),
                request.getRequestURI(),
                appName,
                addressStatistic
        );
    }

    @GetMapping("/events/{" + eventIdAlias + "}")
    public EventResponseDto getPublic(
            @PathVariable(name = eventIdAlias) Long eventId,
            HttpServletRequest request,
            @Value("${application.name}") String appName,
            @Value("${ewm-stats-service.url}") String addressStatistic
    ) {
        return eventService.getPublic(
                eventId,
                request.getRemoteAddr(),
                request.getRequestURI(),
                appName,
                addressStatistic
        );
    }
}
