package ru.practicum.explorewithmestatsserver.error;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.lang.reflect.Method;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    private final String handleName = "handle";

    private ErrorItem handleCustomErrorItem(String responseStatusReason, HttpStatus responseStatus) {
        ErrorItem error = new ErrorItem();
        error.setMessage(responseStatusReason);
        error.setCode(responseStatus.toString());
        log.info(error.toString());
        return error;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MissingServletRequestParameterException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                e.getMessage(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(MethodArgumentTypeMismatchException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "CHECK --> " + e.getName() + ": " + Objects.requireNonNull(e.getValue()),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorItem handle(DataIntegrityViolationException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                "Request error",
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(HitNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorItem handle(HitNotFoundException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }

    @ExceptionHandler(HitBadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorItem handle(HitBadRequestException e) throws NoSuchMethodException {
        Method currentMethod = getClass().getMethod(handleName, e.getClass());
        return handleCustomErrorItem(
                Objects.requireNonNull(AnnotationUtils.getAnnotation(e.getClass(), ResponseStatus.class)).reason(),
                currentMethod.getAnnotation(ResponseStatus.class).value()
        );
    }
}
