package ru.practicum.explorewithmeservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmeservice.category.model.Category;
import ru.practicum.explorewithmeservice.location.model.Location;
import ru.practicum.explorewithmeservice.user.model.User;

import javax.persistence.*;
import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"event_id"})})
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id")
    private Long id;
    @Column(name = "annotation", columnDefinition = "TEXT")
    private String annotation;
    @JoinColumn(name = "category_id")
    @ManyToOne
    private Category category;
    @Column(name = "confirmedRequests")
    private Long confirmedRequests;
    @Column(name = "createdOn")
    private Calendar createdOn;
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    @Column(name = "eventDate")
    private Calendar eventDate;
    @JoinColumn(name = "user_id")
    @OneToOne
    private User initiator;
    @JoinColumn(name = "location_id")
    @OneToOne(cascade = {CascadeType.PERSIST})
    private Location location;
    @Column(name = "paid")
    private Boolean paid;
    @Column(name = "participantLimit")
    private Integer participantLimit;
    @Column(name = "publishedOn")
    private Calendar publishedOn;
    @Column(name = "requestModeration")
    private Boolean requestModeration;
    @Column(name = "state")
    private EventState state;
    @Column(name = "title")
    private String title;
    @Column(name = "views")
    private Long views;
}
