package com.coursework.backend.userGroupRole.dto;

import com.coursework.backend.userGroupRole.model.UserGroupRole;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupRoleDto {
    private String login;
    private String name;
    private UserGroupRole.Role role;

    public static UserGroupRoleDto fromUserGroupRole(UserGroupRole userGroupRole) {
        return UserGroupRoleDto.builder()
                .login(userGroupRole.getUser().getLogin())
                .name(userGroupRole.getUser().getName())
                .role(userGroupRole.getRole())
                .build();
    }
}
