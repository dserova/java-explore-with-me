package ru.practicum.explorewithmeservice.user.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithmeservice.user.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select i from User i " +
            "where " +
            "((i.id IN (:ids)) OR (:ids IS null)) " +
            "ORDER BY i.id ASC")
    Optional<Page<User>> findAllById(
            @Param("ids") List<Long> ids,
            Pageable pageable
    );
}
