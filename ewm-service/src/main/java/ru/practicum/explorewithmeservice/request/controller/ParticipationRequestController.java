package ru.practicum.explorewithmeservice.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestRequestUpdateDto;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestResponseDto;
import ru.practicum.explorewithmeservice.request.dto.ParticipationRequestResponseUpdateDto;
import ru.practicum.explorewithmeservice.request.service.ParticipationRequestService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class ParticipationRequestController {

    private static final String userIdAlias = "userId";
    private static final String eventIdAlias = "eventId";
    private static final String requestIdAlias = "requestsId";
    private final ParticipationRequestService participationRequestService;

    @GetMapping("/users/{" + userIdAlias + "}/events/{" + eventIdAlias + "}/requests")
    public List<ParticipationRequestResponseDto> getRequestsByUserByEvent(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = eventIdAlias) Long eventId
    ) {
        return participationRequestService.getRequestsByUserByEvent(
                userId,
                eventId
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/events/{" + eventIdAlias + "}/requests")
    public ParticipationRequestResponseUpdateDto updateRequestsByUserByEvent(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = eventIdAlias) Long eventId,
            @Valid @RequestBody ParticipationRequestRequestUpdateDto request
    ) {
        return participationRequestService.updateRequestsByUserByEvent(
                userId,
                eventId,
                request
        );
    }

    @GetMapping("/users/{" + userIdAlias + "}/requests")
    public List<ParticipationRequestResponseDto> getRequestsPrivateByUser(
            @PathVariable(name = userIdAlias) Long userId
    ) {
        return participationRequestService.getRequestsPrivateByUser(
                userId
        );
    }

    @PostMapping(value = "/users/{" + userIdAlias + "}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestResponseDto createRequestsPrivateByUser(
            @PathVariable(name = userIdAlias) Long userId,
            @RequestParam(name = eventIdAlias) Long eventId
    ) {
        return participationRequestService.createRequestsPrivateByUser(
                userId,
                eventId
        );
    }

    @PatchMapping("/users/{" + userIdAlias + "}/requests/{" + requestIdAlias + "}/cancel")
    public ParticipationRequestResponseDto updateRequestsPrivateByUserByEventCancel(
            @PathVariable(name = userIdAlias) Long userId,
            @PathVariable(name = requestIdAlias) Long requestsId
    ) {
        return participationRequestService.updateRequestsPrivateByUserByEventCancel(
                userId,
                requestsId
        );
    }

}
