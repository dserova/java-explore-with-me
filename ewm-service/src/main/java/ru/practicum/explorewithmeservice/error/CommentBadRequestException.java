package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Comment error request")
public class CommentBadRequestException extends RuntimeException {
    HttpStatus httpStatus;

    public CommentBadRequestException() {
        super("Comment error request");
    }

    public CommentBadRequestException(String message) {
        super(message);
    }
}
