package ru.practicum.explorewithmeservice.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithmeservice.category.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
