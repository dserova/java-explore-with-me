package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Compilation not found")
public class CompilationNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public CompilationNotFoundException() {
        super("Compilation not found");
    }

    public CompilationNotFoundException(String message) {
        super(message);
    }
}
