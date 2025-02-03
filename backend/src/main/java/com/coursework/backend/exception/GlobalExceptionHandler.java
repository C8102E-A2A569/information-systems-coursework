package com.coursework.backend.exception;
import com.coursework.backend.exception.exceptions.UserAlreadyExistsException;
import com.coursework.backend.exception.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }
}
