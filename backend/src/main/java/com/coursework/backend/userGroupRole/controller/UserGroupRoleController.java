package com.coursework.backend.userGroupRole.controller;

import com.coursework.backend.userGroupRole.model.Role;
import com.coursework.backend.userGroupRole.service.UserGroupRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-group-roles")
@RequiredArgsConstructor
public class UserGroupRoleController {
    private final UserGroupRoleService userGroupRoleService;

    @GetMapping("/user-login/{login}/groups/{groupId}/role")
    public Role getUserRoleInGroup(@PathVariable String login, @PathVariable Long groupId) {
        return userGroupRoleService.getUserRoleInGroup(login, groupId);
    }
}
