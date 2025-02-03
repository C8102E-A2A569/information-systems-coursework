package com.coursework.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LoginUserDto {

    @NotBlank
    @Size(min = 5, max = 15)
    private String login;

    @NotBlank
    @Size(min = 5, max = 15)
    private String name;

    @NotBlank
    @Size(min = 5, max = 15)
    private String password;

}
