package ru.practicum.explorewithmestatsserver.hit.controller;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmestatscommon.dto.EndpointHitDto;
import ru.practicum.explorewithmestatscommon.dto.ViewStatsDto;
import ru.practicum.explorewithmestatsserver.hit.model.EndpointHit;
import ru.practicum.explorewithmestatsserver.hit.service.EndpointHitService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/")
public class EndpointHitController {
    private final ModelMapper mapper;

    private final EndpointHitService hitService;

    @GetMapping("/stats")
    public List<ViewStatsDto> getAllHits(
            @Valid @NotNull @RequestParam(name = "start") Calendar start,
            @Valid @NotNull @RequestParam(name = "end") Calendar end,
            @RequestParam(name = "unique", defaultValue = "false") Boolean unique,
            @RequestParam(name = "uris", required = false) List<String> uris
    ) {
        return hitService.getAllHits(start, end, unique, uris).stream().map(user -> mapper.map(user, ViewStatsDto.class))
                .collect(Collectors.toList());
    }

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public EndpointHitDto createUser(@RequestBody EndpointHitDto userRequestDto) {
        EndpointHit user = hitService.createHit(userRequestDto);
        return mapper.map(user, EndpointHitDto.class);
    }
}
