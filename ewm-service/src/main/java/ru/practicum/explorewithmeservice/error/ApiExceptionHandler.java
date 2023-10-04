package ru.practicum.explorewithmeservice.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private final String handleName = "handle";

    private ErrorItem handleCustomErrorItem(String responseMessage, String responseStatusReason, HttpStatus responseStatus) {
        ErrorItem error = new ErrorItem();
        error.setMessage(responseMessage);
        error.setStatus(responseStatus.toString());
        error.setReason(responseStatusReason);
        error.setTimestamp(Calendar.getInstance());
        log.info(error.toString());
        return error;
    }

    @ExceptionHandler(
            MissingServletRequestParameterException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MissingServletRequestParameterException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getMessage(),
                e.getMessage(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(
            MethodArgumentNotValidException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MethodArgumentNotValidException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(e.getFieldError()).getDefaultMessage(),
                "Incorrect [" + Objects.requireNonNull(e.getFieldError()).getField() + "]",
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(
            MethodArgumentTypeMismatchException.class
    )
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MethodArgumentTypeMismatchException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getMessage(),
                "Incorrect [" + e.getName() + "]: " + Objects.requireNonNull(e.getValue()),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(
            DataIntegrityViolationException.class
    )
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorItem handle(DataIntegrityViolationException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getMessage(),
                "Request error",
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            UserNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(UserNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getLocalizedMessage(),
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            UserBadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(UserBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "incorrect",
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            CategoryNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(CategoryNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getLocalizedMessage(),
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            CategoryBadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(CategoryBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "incorrect",
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            CompilationNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(CompilationNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getLocalizedMessage(),
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            CompilationBadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(CompilationBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "incorrect",
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            EventNotFoundException.class
    })
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(EventNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getLocalizedMessage(),
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            EventBadRequestException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(EventBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "incorrect",
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler({
            EventConflictException.class
    })
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorItem handle(EventConflictException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "incorrect",
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }
}
