package com.coursework.backend.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class GroupUserDto {
    @NotBlank
    private String login;
    @NotBlank
    private String name;
}
