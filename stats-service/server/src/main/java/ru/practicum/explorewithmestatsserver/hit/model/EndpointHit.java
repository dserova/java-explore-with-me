package ru.practicum.explorewithmestatsserver.hit.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.*;
import java.util.Calendar;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "hits", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"hit_id"})})
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "hit_id")
    private Long id;
    @NonNull
    @Column(name = "app")
    private String app;
    @NonNull
    @Column(name = "uri")
    private String uri;
    @NonNull
    @Column(name = "ip")
    private String ip;
    @NonNull
    @Column(name = "timestamp")
    private Calendar timestamp;
}
