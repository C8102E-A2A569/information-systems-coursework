package com.coursework.backend.user.dto;

import com.coursework.backend.userGroupRole.model.UserGroupRole;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GroupUserDto {
    private String login;

    private String name;

    private UserGroupRole.Role role;
}
