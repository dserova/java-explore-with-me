package ru.practicum.explorewithmestatsserver.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Item not found")
public class HitNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public HitNotFoundException() {
        super();
    }

}
