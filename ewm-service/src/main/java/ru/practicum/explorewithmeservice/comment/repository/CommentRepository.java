package ru.practicum.explorewithmeservice.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithmeservice.comment.model.Comment;
import ru.practicum.explorewithmeservice.comment.model.CommentStatus;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    Optional<Comment> findByIdAndAuthor_Id(Long commentId, Long authorId);

    @Query("select c from Comment c " +
            "where " +
            "c.id in (:commentIds) " +
            "and " +
            "c.event.id = :eventId " +
            "and " +
            "c.event.initiator.id = :userId ")
    List<Comment> updateMulti(
            Long userId,
            Long eventId,
            List<Long> commentIds
    );

    Page<Comment> findAllByEvent_IdAndEvent_Initiator_Id(Long eventId, Long userId, Pageable pageable);

    Optional<Comment> findByRepliesContains(Comment replies);

    Page<Comment> findAllByAuthor_Id(Long userId, Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "  where" +
            "                  (c.event.id = :eventId " +
            "                  OR " +
            "                  ((:eventId) IS null)) " +
            "  and " +
            "                  (c.status = :status " +
            "                  OR " +
            "                  ((:status) IS null)) ")
    Page<Comment> getAllByEventIdAndStatus(Long eventId, CommentStatus status, Pageable pageable);

    @Query("select c " +
            "from Comment c " +
            "  where" +
            "                  (upper(c.text) like " +
            "                  upper(concat('%' , :text , '%')) " +
            "                  OR " +
            "                  ((:text) is null)) " +
            "  and " +
            "                  (c.author.id in (:users) " +
            "                  OR " +
            "                  ((:users) IS null)) " +
            "  and " +
            "                  (c.event.id in (:events) " +
            "                  OR " +
            "                  ((:events) IS null)) " +
            "  and " +
            "                  (c.created >= (:rangeCreateStart) " +
            "                  OR " +
//            "                  ((:rangeCreateStart) IS null)) " +
            "                  (COALESCE(:rangeCreateStart,null) IS null)) " +
            "  and " +
            "                  (c.created <= (:rangeCreateEnd) " +
            "                  OR " +
//            "                  ((:rangeCreateEnd) IS null)) " +
            "                  (COALESCE(:rangeCreateEnd,null) IS null)) " +
            "  and " +
            "                  (c.updated >= (:rangeUpdateStart) " +
            "                  OR " +
//            "                  ((:rangeUpdateStart) IS null)) " +
            "                  (COALESCE(:rangeUpdateStart,null) IS null)) " +
            "  and " +
            "                  (c.updated <= (:rangeUpdateEnd) " +
            "                  OR " +
//            "                  ((:rangeUpdateEnd) IS null)) " +
            "                  (COALESCE(:rangeUpdateEnd,null) IS null)) " +
            "  and " +
            "                  (c.status = :status " +
            "                  OR " +
            "                  ((:status) IS null)) " +
            "  and " +
            "                  (c.id NOT IN (select elements(CNR.replies) from Comment CNR) " +
            "                  OR " +
            "                  ((:onlyNotReply) IS null)" +
            "                  OR " +
            "                  ((:onlyNotReply) = false)) ")
    Page<Comment> getComments(
            @Param("text") String text,
            @Param("users") List<Long> users,
            @Param("events") List<Long> events,
            @Param("rangeCreateStart") Calendar rangeCreateStart,
            @Param("rangeCreateEnd") Calendar rangeCreateEnd,
            @Param("rangeUpdateStart") Calendar rangeUpdateStart,
            @Param("rangeUpdateEnd") Calendar rangeUpdateEnd,
            @Param("onlyNotReply") Boolean onlyNotReply,
            @Param("status") CommentStatus status,
            Pageable pageable
    );
}
