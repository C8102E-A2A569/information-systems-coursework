package com.coursework.backend.user.dto;
import lombok.Getter;

@Getter
public class AuthResponseDto {
    private String login;
    private String name;
    private String token;
    private final String tokenType = "Bearer ";

    public AuthResponseDto(String login, String name, String token) {
        this.login = login;
        this.name = name;
        this.token = token;
    }
}
