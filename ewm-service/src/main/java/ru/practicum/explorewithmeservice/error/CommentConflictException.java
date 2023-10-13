package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Comment error request")
public class CommentConflictException extends RuntimeException {
    HttpStatus httpStatus;

    public CommentConflictException() {
        super("Comment error request");
    }

    public CommentConflictException(String message) {
        super(message);
    }
}
