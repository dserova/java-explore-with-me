package ru.practicum.explorewithmeservice.category.service;

import ru.practicum.explorewithmeservice.category.dto.CategoryRequestDto;
import ru.practicum.explorewithmeservice.category.dto.CategoryResponseDto;

import java.util.List;

public interface CategoryService {
    CategoryResponseDto updateCategory(
            Long categoryId,
            CategoryRequestDto categoryRequestDto
    );

    void deleteCategory(
            Long categoryId
    );

    CategoryResponseDto createCategory(
            CategoryRequestDto categoryRequestDto
    );

    CategoryResponseDto getCategoryById(
            Long categoryId
    );

    List<CategoryResponseDto> getAllCategories(
            Integer start,
            Integer size
    );

}