package com.coursework.backend.group.dto;

import com.coursework.backend.userGroupRole.dto.UserGroupRoleDto;
import com.coursework.backend.userGroupRole.model.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class GroupDto {
    private Long id;
    private String name;
    private Set<UserGroupRoleDto> users;
}
