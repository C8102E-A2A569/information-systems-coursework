package com.coursework.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginUserDto {

    @NotBlank
    private String login;

    @NotBlank
    @NotNull(message = "Password cannot be null")
    private String password;

}
