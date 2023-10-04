package ru.practicum.explorewithmeservice.location.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "locationss", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"location_id"})})
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "location_id")
    private Long id;
    @JoinColumn(name = "lat")
    private Float lat;
    @JoinColumn(name = "lon")
    private Float lon;
}
