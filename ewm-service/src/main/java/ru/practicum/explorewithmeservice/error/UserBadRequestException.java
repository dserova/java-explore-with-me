package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "User error request")
public class UserBadRequestException extends RuntimeException {
    HttpStatus httpStatus;

    public UserBadRequestException() {
        super("User error request");
    }

    public UserBadRequestException(String message) {
        super(message);
    }
}
