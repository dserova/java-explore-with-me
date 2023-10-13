package ru.practicum.explorewithmeservice.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.event.model.EventState;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findAllByInitiator_id(
            Long initiatorId,
            Pageable pageable
    );

    Optional<Event> findByIdAndInitiator_id(
            Long id,
            Long initiatorId
    );

    @Query("select new ru.practicum.explorewithmeservice.event.model.Event( " +
            "e.id, " +
            "e.annotation, " +
            "e.category, " +
            "count(req.id), " +
            "e.createdOn, " +
            "e.description, " +
            "e.eventDate, " +
            "e.initiator, " +
            "e.location, " +
            "e.paid, " +
            "e.participantLimit, " +
            "e.publishedOn, " +
            "e.requestModeration, " +
            "e.state, " +
            "e.title, " +
            "e.views) " +
            "from Event e " +
            "          LEFT JOIN ParticipationRequest req" +
            "          ON e.id = req.event.id" +
            "  and " +
            "req.status = ru.practicum.explorewithmeservice.request.model.EventRequestState.CONFIRMED " +
            "where " +
            "e.id = :eventId " +
            "and " +
            "e.state = ru.practicum.explorewithmeservice.event.model.EventState.PUBLISHED " +
            "group by e,req.event")
    Optional<Event> getPublic(
            @Param("eventId") Long eventId
    );

    @Query("select new ru.practicum.explorewithmeservice.event.model.Event( " +
            "e.id, " +
            "e.annotation, " +
            "e.category, " +
            "count(req.id), " +
            "e.createdOn, " +
            "e.description, " +
            "e.eventDate, " +
            "e.initiator, " +
            "e.location, " +
            "e.paid, " +
            "e.participantLimit, " +
            "e.publishedOn, " +
            "e.requestModeration, " +
            "e.state, " +
            "e.title, " +
            "e.views) " +
            "from Event e " +
            "          LEFT JOIN ParticipationRequest req" +
            "          ON e.id = req.event.id" +
            "  and " +
            "req.status = ru.practicum.explorewithmeservice.request.model.EventRequestState.CONFIRMED " +
            "  where" +
            "                  (upper(concat(e.title , ',,,' , e.description , ',,,' , e.annotation)) like " +
            "                  upper(concat('%' , :text , '%')) " +
            "                  OR " +
            "                  ((:text) is null)) " +
            "  and " +
            "                  (e.category.id in (:categories) " +
            "                  OR " +
            "                  ((:categories) IS null)) " +
            "  and " +
            "                  (e.paid = (:paid) " +
            "                  OR " +
            "                  ((:paid) IS null)) " +
            "  and " +
            "                  (e.eventDate >= (:rangeStart) " +
            "                  OR " +
//            "                  ((:rangeStart) IS null)) " +
            "                  (COALESCE(:rangeStart,null) IS null)) " +
            "  and " +
            "                  (e.eventDate <= (:rangeEnd) " +
            "                  OR " +
//            "                  ((:rangeEnd) IS null)) " +
            "                  (COALESCE(:rangeEnd,null) IS null)) " +
            "  and " +
            "                  e.state = ru.practicum.explorewithmeservice.event.model.EventState.PUBLISHED " +
            "group by e,req.event " +
            "  HAVING " +
            "                  (count(req.id) < e.participantLimit " +
            "                  OR " +
            "                  ((:onlyAvailable) <> true)) " +
            "order by " +
            "CASE WHEN (COALESCE(:sortState, null) IS null) THEN null END ASC, " +
            "CASE WHEN (COALESCE(:sortState, null) = 'EVENT_DATE') THEN e.eventDate END ASC, " +
            "CASE WHEN (COALESCE(:sortState, null) = 'VIEWS') THEN e.views END ASC")
    Page<Event> search(
            @Param("text") String text,
            @Param("categories") List<Long> categories,
            @Param("paid") Boolean paid,
            @Param("rangeStart") Calendar rangeStart,
            @Param("rangeEnd") Calendar rangeEnd,
            @Param("sortState") String sortState,
            @Param("onlyAvailable") Boolean onlyAvailable,
            Pageable pageable
    );

    @Query("select new ru.practicum.explorewithmeservice.event.model.Event( " +
            "e.id, " +
            "e.annotation, " +
            "e.category, " +
            "count(req.id), " +
            "e.createdOn, " +
            "e.description, " +
            "e.eventDate, " +
            "e.initiator, " +
            "e.location, " +
            "e.paid, " +
            "e.participantLimit, " +
            "e.publishedOn, " +
            "e.requestModeration, " +
            "e.state, " +
            "e.title, " +
            "e.views) " +
            "from Event e " +
            "          LEFT JOIN ParticipationRequest req" +
            "          ON e.id = req.event.id" +
            "  where" +
            "                  (e.initiator.id in (:users) " +
            "                  OR " +
            "                  ((:users) is null)) " +
            "  and " +
            "                  (e.category.id in (:categories) " +
            "                  OR " +
            "                  ((:categories) IS null)) " +
            "  and " +
            "                  (e.state in (:states) " +
            "                  OR " +
            "                  ((:states) IS null)) " +
            "  and " +
            "                  (e.eventDate >= (:rangeStart) " +
            "                  OR " +
//            "                  ((:rangeStart) IS null)) " +
            "                  (COALESCE(:rangeStart,null) IS NULL)) " +
            "  and " +
            "                  (e.eventDate <= (:rangeEnd) " +
            "                  OR " +
//            "                  ((:rangeEnd) IS null)) " +
            "                  (COALESCE(:rangeEnd,null) IS NULL)) " +
            "group by e,req.event")
    Page<Event> searchAdmin(
            @Param("users") List<Long> users,
            @Param("states") List<EventState> states,
            @Param("categories") List<Long> categories,
            @Param("rangeStart") Calendar rangeStart,
            @Param("rangeEnd") Calendar rangeEnd,
            Pageable pageable
    );
}
