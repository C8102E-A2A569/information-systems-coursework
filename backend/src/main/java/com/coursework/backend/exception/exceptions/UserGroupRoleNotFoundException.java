package com.coursework.backend.exception.exceptions;

public class UserGroupRoleNotFoundException extends RuntimeException {
    public UserGroupRoleNotFoundException(String message, String login, Long groupId) {
        super(message);
    }
}
