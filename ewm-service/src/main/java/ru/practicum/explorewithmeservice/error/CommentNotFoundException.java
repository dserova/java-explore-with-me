package ru.practicum.explorewithmeservice.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(reason = "Comment not found")
public class CommentNotFoundException extends RuntimeException {
    HttpStatus httpStatus;

    public CommentNotFoundException() {
        super("Comment not found");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
