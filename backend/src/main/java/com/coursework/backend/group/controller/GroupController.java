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
        return groupService.createGroup(dto, userLogin);
    }

    @DeleteMapping("/delete/{groupId}/users/{userLogin}/admin/{adminLogin}")
    public void deleteGroup(@PathVariable Long groupId, @PathVariable String userLogin, @PathVariable String adminLogin) {
        groupService.deleteGroup(groupId, adminLogin);
    }

    @PostMapping("/add-user/{groupId}/users/{userLogin}/admin/{adminLogin}")
    public void addUserToGroup(@PathVariable Long groupId, @PathVariable String userLogin, @PathVariable String adminLogin) {
        groupService.addUserToGroup(groupId, userLogin, adminLogin);
    }

    @DeleteMapping("/remove-user/{groupId}/users/{userLogin}/admin/{adminLogin}")
    public void removeUserFromGroup(@PathVariable Long groupId, @PathVariable String userLogin, @PathVariable String adminLogin) {
        groupService.removeUserFromGroup(groupId, userLogin, adminLogin);
    }

}

