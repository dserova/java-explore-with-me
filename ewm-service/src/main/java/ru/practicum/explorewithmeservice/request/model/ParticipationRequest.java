package ru.practicum.explorewithmeservice.request.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmeservice.event.model.Event;
import ru.practicum.explorewithmeservice.user.model.User;

import javax.persistence.*;
import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "requests", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"request_id"})})
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    @Column(name = "created")
    private Calendar created;
    @JoinColumn(name = "event_id")
    @ManyToOne
    private Event event;
    @JoinColumn(name = "user_id")
    @ManyToOne
    private User requester;
    @Column(name = "status")
    private EventRequestState status;
}
