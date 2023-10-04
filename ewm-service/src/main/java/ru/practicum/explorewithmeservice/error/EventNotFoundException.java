package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Event not found")
public class EventNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public EventNotFoundException() {
        super("User not found");
    }

    public EventNotFoundException(String message) {
        super(message);
    }
}
