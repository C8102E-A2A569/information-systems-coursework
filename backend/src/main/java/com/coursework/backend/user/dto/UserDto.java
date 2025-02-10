package com.coursework.backend.user.dto;

import com.coursework.backend.group.dto.GroupDto;
import com.coursework.backend.user.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private Set<GroupDto> groups;

    public static UserDto fromEntity(User user) {
        Set<GroupDto> groups = user.getUserGroupRoles().stream()
                .map(ugr -> new GroupDto(
                        ugr.getGroup().getId(),
                        ugr.getGroup().getName(),
                        Collections.emptySet()
                ))
                .collect(Collectors.toSet());
        return new UserDto(user.getLogin(), user.getName(), groups);
    }
}
