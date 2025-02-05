package com.coursework.backend.userGroupRole.dto;

import com.coursework.backend.userGroupRole.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserGroupRoleDto {
    private String login;
    private String name;
    private Role role;
}
