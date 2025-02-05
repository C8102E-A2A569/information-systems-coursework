package com.coursework.backend.userGroupRole.controller;

import com.coursework.backend.userGroupRole.model.Role;
import com.coursework.backend.userGroupRole.service.UserGroupRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-group-roles")
@RequiredArgsConstructor
public class UserGroupRoleController {

    private final UserGroupRoleService userGroupRoleService;

//    @GetMapping("/user-login/{login}/groups/{groupId}/role")
//    public Role getUserRoleInGroup(@PathVariable String login, @PathVariable Long groupId) {
//        return userGroupRoleService.getUserRoleInGroup(login, groupId);
//    }
//    @PostMapping("/add-role/{groupId}/users/{userLogin}")
//    public void addUserToGroupWithRole(@PathVariable Long groupId, @PathVariable String userLogin, @RequestParam Role role) {
//        userGroupRoleService.addUserRoleToGroup(groupId, userLogin, role);
//    }
}

