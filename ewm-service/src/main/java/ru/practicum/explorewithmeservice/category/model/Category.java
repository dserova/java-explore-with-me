package ru.practicum.explorewithmeservice.category.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "categories", schema = "Public", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(name = "name")
    private String name;
}
