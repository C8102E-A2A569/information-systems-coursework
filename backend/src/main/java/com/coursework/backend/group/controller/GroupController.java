package com.coursework.backend.group.controller;

import com.coursework.backend.group.dto.CreateGroupDto;
import com.coursework.backend.group.dto.GroupDto;
import com.coursework.backend.group.dto.GroupInListResponse;
import com.coursework.backend.group.dto.PatchGroupDto;
import com.coursework.backend.group.service.GroupService;
import com.coursework.backend.user.dto.GroupUserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    @GetMapping("/{groupId}/users")
    public List<GroupUserDto> getUsersInGroup(@PathVariable Long groupId) {
        return groupService.getUsersInGroup(groupId);
    }

    @PostMapping("/create")
    public GroupDto createGroup(@RequestBody @Valid CreateGroupDto createGroupDto) {
        return groupService.createGroup(createGroupDto);
    }

    @PatchMapping("/{groupId}/update-name")
    public GroupDto updateGroupName(@PathVariable Long groupId,
                                    @RequestBody @Valid PatchGroupDto patchGroupDto) {
        return groupService.updateGroupName(groupId, patchGroupDto);
    }

    @DeleteMapping("/delete/{groupId}")
    public void deleteGroup(@PathVariable Long groupId) {
        groupService.deleteGroup(groupId);
    }

    @PostMapping("/add-user/{groupId}/users/{userLogin}")
    public GroupDto addUserToGroup(@PathVariable Long groupId, @PathVariable String userLogin) {
        return groupService.addUserToGroup(groupId, userLogin);
    }

    @DeleteMapping("/remove-user/{groupId}/users/{userLogin}")
    public GroupDto removeUserFromGroup(@PathVariable Long groupId, @PathVariable String userLogin) {
        return groupService.removeUserFromGroup(groupId, userLogin);
    }

    @GetMapping("/my")
    public List<GroupInListResponse> getMyGroups() {
        return groupService.getUserGroups();
    }


}
