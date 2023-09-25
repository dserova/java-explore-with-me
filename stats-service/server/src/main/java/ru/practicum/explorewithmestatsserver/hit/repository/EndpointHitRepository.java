package ru.practicum.explorewithmestatsserver.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithmestatscommon.dto.ViewStatsDto;
import ru.practicum.explorewithmestatsserver.hit.model.EndpointHit;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {
    @Query("select new ru.practicum.explorewithmestatscommon.dto.ViewStatsDto(i.app, i.uri, count(i.id) ) from EndpointHit i " +
            "where " +
            "i.timestamp >= :start " +
            "and " +
            "i.timestamp < :end " +
            "and " +
            "((trim(i.uri) IN (:uris)) OR (NULLIF((:uris), null) IS null)) " +
            "group by i.app, i.uri ORDER BY count(i.id) DESC")
    Optional<List<ViewStatsDto>> getAllHitsWithFilter(
            @Param("start") Calendar start,
            @Param("end") Calendar end,
            @Param("uris") List<String> uris
    );

    @Query("select new ru.practicum.explorewithmestatscommon.dto.ViewStatsDto(i.app, i.uri, 1L) from EndpointHit i " +
            "where " +
            "i.timestamp >= :start " +
            "and " +
            "i.timestamp < :end " +
            "and " +
            "((trim(i.uri) IN (:uris)) OR (NULLIF((:uris), null) IS null)) " +
            "group by i.app, i.uri ORDER BY i.app, i.uri DESC")
    Optional<List<ViewStatsDto>> getAllHitsWithFilterUnique(
            @Param("start") Calendar start,
            @Param("end") Calendar end,
            @Param("uris") List<String> uris
    );
}
