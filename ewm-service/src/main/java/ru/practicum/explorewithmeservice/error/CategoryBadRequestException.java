package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Category error request")
public class CategoryBadRequestException extends RuntimeException {
    HttpStatus httpStatus;

    public CategoryBadRequestException() {
        super("Category error request");
    }

    public CategoryBadRequestException(String message) {
        super(message);
    }

}
