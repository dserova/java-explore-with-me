package ru.practicum.explorewithmeservice.category.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.explorewithmeservice.category.dto.CategoryRequestDto;
import ru.practicum.explorewithmeservice.category.dto.CategoryResponseDto;
import ru.practicum.explorewithmeservice.category.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping
public class CategoryController {

    private static final String fromRequestParam = "from";
    private static final String sizeRequestParam = "size";
    private static final String categoryIdAlias = "categoryId";
    private final CategoryService categoryService;

    @PostMapping(value = "/admin/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(
            @Valid @RequestBody CategoryRequestDto request
    ) {
        return categoryService.createCategory(
                request
        );
    }

    @PatchMapping("/admin/categories/{" + categoryIdAlias + "}")
    public CategoryResponseDto updateCategory(
            @PathVariable(name = categoryIdAlias) Long categoryId,
            @Valid @RequestBody CategoryRequestDto request
    ) {
        return categoryService.updateCategory(
                categoryId,
                request
        );
    }

    @DeleteMapping("/admin/categories/{" + categoryIdAlias + "}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(
            @PathVariable(name = categoryIdAlias) Long categoryId
    ) {
        categoryService.deleteCategory(
                categoryId
        );
    }

    @GetMapping("/categories")
    public List<CategoryResponseDto> getAllCategories(
            @PositiveOrZero @RequestParam(name = fromRequestParam, defaultValue = "0") int from,
            @Positive @RequestParam(name = sizeRequestParam, defaultValue = "10") int size
    ) {
        return categoryService.getAllCategories(
                from,
                size
        );
    }

    @GetMapping("/categories/{" + categoryIdAlias + "}")
    public CategoryResponseDto getCategoryById(
            @PathVariable(name = categoryIdAlias) Long categoryId
    ) {
        return categoryService.getCategoryById(
                categoryId
        );
    }

}
