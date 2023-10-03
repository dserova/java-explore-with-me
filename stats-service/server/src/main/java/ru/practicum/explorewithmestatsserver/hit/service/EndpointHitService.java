package ru.practicum.explorewithmestatsserver.hit.service;

import ru.practicum.explorewithmestatscommon.dto.EndpointHitDto;
import ru.practicum.explorewithmestatscommon.dto.ViewStatsDto;
import ru.practicum.explorewithmestatsserver.hit.model.EndpointHit;

import java.util.Calendar;
import java.util.List;

public interface EndpointHitService {
    List<ViewStatsDto> getAllHits(
            Calendar start,
            Calendar end,
            Boolean unique,
            List<String> uris
    );

    EndpointHit createHit(
            EndpointHitDto hit
    );
}