package com.coursework.backend.group.dto;

import com.coursework.backend.userGroupRole.model.UserGroupRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupInListResponse {
    private Long id;

    private String name;

    private UserGroupRole.Role role;

    public static GroupInListResponse fromUserGroupRole(UserGroupRole userGroupRole) {
        return GroupInListResponse.builder()
                .id(userGroupRole.getGroup().getId())
                .name(userGroupRole.getGroup().getName())
                .role(userGroupRole.getRole())
                .build();
    }
}
