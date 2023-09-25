package ru.practicum.explorewithmestatsserver.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Error request")
public class HitBadRequestException extends RuntimeException {
    HttpStatus httpStatus;

    public HitBadRequestException() {
        super();
    }

}
