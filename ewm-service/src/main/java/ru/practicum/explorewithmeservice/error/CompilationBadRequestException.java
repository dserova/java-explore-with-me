package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Compilation error request")
public class CompilationBadRequestException extends RuntimeException {
    HttpStatus httpStatus;

    public CompilationBadRequestException() {
        super("Compilation error request");
    }

    public CompilationBadRequestException(String message) {
        super(message);
    }

}
