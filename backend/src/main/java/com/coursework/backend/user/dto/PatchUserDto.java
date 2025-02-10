package com.coursework.backend.user.dto;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PatchUserDto {
    @Size(min = 5, max = 15)
    private String login;

    @Size(min = 5, max = 15)
    private String name;

    @Size(min = 5, max = 15)
    private String password;
}
