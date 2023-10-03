package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Event error request")
public class EventConflictException extends RuntimeException {
    HttpStatus httpStatus;

    public EventConflictException() {
        super("User error request");
    }

    public EventConflictException(String message) {
        super(message);
    }
}
