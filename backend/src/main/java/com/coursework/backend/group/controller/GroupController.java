package com.coursework.backend.group.controller;

import com.coursework.backend.group.dto.CreateGroupDto;
import com.coursework.backend.group.dto.GroupDto;
import com.coursework.backend.group.service.GroupService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {
    private final GroupService groupService;

    @PostMapping
    public GroupDto createGroup(@RequestParam String userLogin, @RequestBody @Valid CreateGroupDto dto) {
        return groupService.createGroup(userLogin, dto);
    }

    @DeleteMapping("/delete/{groupId}/users/{userLogin}")
    public void deleteGroup(@PathVariable Long groupId, @PathVariable String userLogin) {
        groupService.deleteGroup(groupId, userLogin);
    }

    @PostMapping("/add-user/{groupId}/users/{userLogin}")
    public void addUserToGroup(@PathVariable Long groupId, @PathVariable String userLogin) {
        groupService.addUserToGroup(groupId, userLogin);
    }

    @DeleteMapping("/remove-user/{groupId}/users/{userLogin}")
    public void removeUserFromGroup(@PathVariable Long groupId, @PathVariable String userLogin) {
        groupService.removeUserFromGroup(groupId, userLogin);
    }
}
