package com.coursework.backend.exception;
import com.coursework.backend.exception.exceptions.*;
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

    @ExceptionHandler(UserAlreadyInGroupException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyInGroupException(UserAlreadyInGroupException e) {
        return new ErrorResponse(
                "UserAlreadyInGroup",
                e.getMessage()
        );
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(UserNotFoundException e) {
        return new ErrorResponse(
                "UserNotFound",
                e.getMessage()
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleInvalidCredentialsException(IllegalArgumentException e) {
        return new ErrorResponse(
                "InvalidCredentials",
                e.getMessage()
        );
    }

    @ExceptionHandler(GroupNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleGroupNotFoundException(GroupNotFoundException e) {
        return new ErrorResponse(
                "GroupNotFound",
                e.getMessage()
        );
    }

    @ExceptionHandler(UserNotInGroupException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleUserNotInGroupException(UserNotInGroupException e) {
        return new ErrorResponse(
                "UserNotInGroup",
                e.getMessage()
        );
    }

    @ExceptionHandler(UserGroupRoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserGroupRoleNotFoundException(UserGroupRoleNotFoundException e) {
        return new ErrorResponse(
                "UserGroupRoleNotFound",
                e.getMessage()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleAccessDeniedException(AccessDeniedException e) {
        return new ErrorResponse(
                e.getClass().getCanonicalName(),
                e.getMessage()
        );
    }

}
