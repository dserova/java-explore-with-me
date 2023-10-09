package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Event error request")
public class EventBadRequestException extends RuntimeException {
    HttpStatus httpStatus;

    public EventBadRequestException() {
        super("Event error request");
    }

    public EventBadRequestException(String message) {
        super(message);
    }
}
