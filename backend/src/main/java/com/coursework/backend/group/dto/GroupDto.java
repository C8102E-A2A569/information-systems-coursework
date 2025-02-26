package com.coursework.backend.group.dto;

import com.coursework.backend.group.model.Group;
import com.coursework.backend.userGroupRole.dto.UserGroupRoleDto;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupDto {
    private Long id;
    private String name;
    private Set<UserGroupRoleDto> users;

    public static GroupDto fromGroup(Group group) {
        final var users = group.getUserGroupRoles().stream()
                .map(UserGroupRoleDto::fromUserGroupRole).collect(Collectors.toSet());
        return GroupDto.builder()
                .id(group.getId())
                .name(group.getName())
                .users(users)
                .build();
    }
}
