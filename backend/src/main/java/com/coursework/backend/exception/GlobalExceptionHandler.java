package com.coursework.backend.exception;
import com.coursework.backend.exception.exceptions.UserAlreadyExistsException;
import com.coursework.backend.exception.exceptions.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return new ErrorResponse(
                "UserAlreadyExists",
                e.getMessage()
        );
    }

//    @ExceptionHandler(UserNotFoundException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
//        return new ErrorResponse(
//                "UserNotFound",
//                e.getMessage()
//        );
//    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(IllegalArgumentException e) {
        return new ErrorResponse(
                "InvalidCredentials",
                e.getMessage()
        );
    }
}
