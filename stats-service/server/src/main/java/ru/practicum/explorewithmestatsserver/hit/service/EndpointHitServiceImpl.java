package ru.practicum.explorewithmestatsserver.hit.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmestatscommon.dto.EndpointHitDto;
import ru.practicum.explorewithmestatscommon.dto.ViewStatsDto;
import ru.practicum.explorewithmestatsserver.error.HitBadRequestException;
import ru.practicum.explorewithmestatsserver.hit.model.EndpointHit;
import ru.practicum.explorewithmestatsserver.hit.repository.EndpointHitRepository;

import java.util.Calendar;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {
    private final EndpointHitRepository endpointHitRepository;

    private final ModelMapper mapper;

    @Override
    public List<ViewStatsDto> getAllHits(Calendar start, Calendar end, Boolean unique, List<String> uris) {
        if (unique) {
            return endpointHitRepository.getAllHitsWithFilterUnique(start, end, uris)
                    .orElseThrow(HitBadRequestException::new);
        } else {
            return endpointHitRepository.getAllHitsWithFilter(start, end, uris)
                    .orElseThrow(HitBadRequestException::new);
        }
    }

    @Override
    public EndpointHit createHit(EndpointHitDto hitDto) {
        EndpointHit hit = mapper.map(hitDto, EndpointHit.class);
        return endpointHitRepository.save(hit);
    }
}


