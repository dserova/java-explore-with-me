package ru.practicum.explorewithmeservice.compilation.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.explorewithmeservice.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "compilations", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"compilation_id"})})
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "compilation_id")
    private Long id;
    @Column(name = "pinned")
    private Boolean pinned;
    @Column(name = "title")
    private String title;
    @JoinColumn(name = "event_id")
    @ManyToMany
    private List<Event> events;
}
