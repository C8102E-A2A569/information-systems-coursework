package com.coursework.backend.user.dto;
import lombok.Getter;

import java.util.Map;

@Getter
public class AuthResponseDto {
    private String login;
    private String name;
    private String token;
    private final String tokenType = "Bearer ";
    private Map<Long, String> groupsWithRoles;  // Группы с ролями

    public AuthResponseDto(String login, String name, String token, Map<Long, String> groupsWithRoles) {
        this.login = login;
        this.name = name;
        this.token = token;
        this.groupsWithRoles = groupsWithRoles;
    }
}
