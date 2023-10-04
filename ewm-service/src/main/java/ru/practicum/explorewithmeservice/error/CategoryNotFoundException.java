package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Category not found")
public class CategoryNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public CategoryNotFoundException() {
        super("Category not found");
    }

    public CategoryNotFoundException(String message) {
        super(message);
    }
}
