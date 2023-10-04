package ru.practicum.explorewithmeservice.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithmeservice.category.dto.CategoryRequestDto;
import ru.practicum.explorewithmeservice.category.dto.CategoryResponseDto;
import ru.practicum.explorewithmeservice.category.model.Category;
import ru.practicum.explorewithmeservice.category.repository.CategoryRepository;
import ru.practicum.explorewithmeservice.error.CategoryNotFoundException;
import ru.practicum.explorewithmeservice.helpers.Helper;
import ru.practicum.explorewithmeservice.helpers.Paging;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final Paging paging = new Paging();

    private final Helper helper = new Helper();

    @Override
    public CategoryResponseDto updateCategory(Long categoryId, CategoryRequestDto data) {
        Function<Category, Category> chain = Function.identity();
        return chain.andThen(
                updating -> {
                    Optional.ofNullable(data.getName()).ifPresent(updating::setName);
                    return updating;
                }
        ).andThen(
                categoryRepository::save
        ).andThen(
                helper.to(CategoryResponseDto.class)
        ).apply(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException("Check your parameters in the path."))
        );
    }

    @Override
    public CategoryResponseDto createCategory(CategoryRequestDto data) {
        Function<CategoryRequestDto, CategoryRequestDto> chain = Function.identity();
        return chain.andThen(
                helper.to(Category.class)
        ).andThen(
                categoryRepository::save
        ).andThen(
                helper.to(CategoryResponseDto.class)
        ).apply(
                data
        );
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }

    @Override
    public CategoryResponseDto getCategoryById(Long categoryId) {
        Function<Category, Category> chain = Function.identity();
        return chain.andThen(
                helper.to(CategoryResponseDto.class)
        ).apply(
                categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new CategoryNotFoundException("Check your parameters in the path."))
        );
    }

    @Override
    public List<CategoryResponseDto> getAllCategories(Integer start, Integer size) {
        Function<Page<Category>, Page<Category>> chain = Function.identity();
        return chain.andThen(
                helper.fromPage(CategoryResponseDto.class)
        ).apply(
                categoryRepository.findAll(paging.getPageable(start, size))
        );
    }

}


