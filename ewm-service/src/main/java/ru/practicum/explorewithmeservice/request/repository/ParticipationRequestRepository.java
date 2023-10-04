package ru.practicum.explorewithmeservice.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithmeservice.request.model.EventRequestState;
import ru.practicum.explorewithmeservice.request.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    Optional<List<ParticipationRequest>> findAllByEvent_IdAndRequester_Id(
            Long eventId,
            Long requesterId
    );

    List<ParticipationRequest> findAllByEvent_IdAndEvent_Initiator_Id(
            Long eventId,
            Long requesterId
    );

    @Query("select pr from ParticipationRequest pr " +
            "where " +
            "pr.id in (:requestIds) " +
            "and " +
            "pr.event.id = :eventId " +
            "and " +
            "pr.event.initiator.id = :userId ")
    List<ParticipationRequest> findRequestForUpdate(
            Long userId,
            Long eventId,
            List<Long> requestIds
    );

    Long countByEvent_IdAndStatus(
            Long eventId,
            EventRequestState status
    );

    List<ParticipationRequest> findAllByRequester_Id(
            Long requesterId
    );

}
