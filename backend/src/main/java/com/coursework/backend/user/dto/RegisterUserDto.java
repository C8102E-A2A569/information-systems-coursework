package com.coursework.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RegisterUserDto {

    @NotBlank
    @Size(min = 4, max = 25)
    private String login;

    @NotBlank
    @Size(min = 4, max = 30)
    private String name;

    @NotBlank
    @NotNull(message = "Password cannot be null")
    @Size(min = 4, max = 20)
    private String password;

}
