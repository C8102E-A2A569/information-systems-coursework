package com.coursework.backend.group.service;

import com.coursework.backend.exception.exceptions.*;
import com.coursework.backend.group.dto.CreateGroupDto;
import com.coursework.backend.group.dto.GroupDto;
import com.coursework.backend.group.dto.GroupInListResponse;
import com.coursework.backend.group.dto.PatchGroupDto;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.group.repository.GroupRepository;
import com.coursework.backend.user.dto.GroupUserDto;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import com.coursework.backend.user.service.UserService;
import com.coursework.backend.userGroupRole.dto.UserGroupRoleDto;
import com.coursework.backend.userGroupRole.model.UserGroupRole;
import com.coursework.backend.userGroupRole.repository.UserGroupRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final UserService userService;

    //список пользователей в группе
    public Set<GroupUserDto> getUsersInGroup(Long groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new GroupNotFoundException("Группа не найдена");
        }

        // Directly query for UserGroupRole entries for this group
        List<UserGroupRole> roles = userGroupRoleRepository.findAllByGroupId(groupId);

        return roles.stream()
                .map(userGroupRole -> new GroupUserDto(
                        userGroupRole.getUser().getLogin(),
                        userGroupRole.getUser().getName()
                ))
                .collect(Collectors.toSet());
    }

    @Transactional
    public GroupDto createGroup(CreateGroupDto createGroupDto) {
        // Создаем новую группу
        Group group = new Group();
        group.setName(createGroupDto.getName());
        group = groupRepository.save(group);

        User currentUser = userService.getCurrentUser();

        // Создание UserGroupRole с правильно установленным составным ключом
        UserGroupRole userGroupRole = new UserGroupRole();
//        UserGroupRoleId userGroupRoleId = new UserGroupRoleId(group.getId(), user.getLogin());
        final var userGroupRoleId = UserGroupRole.UserGroupRoleId.builder()
                .groupId(group.getId())
                .userLogin(currentUser.getLogin())
                .build();
        userGroupRole.setId(userGroupRoleId);  // Устанавливаем составной ключ
        userGroupRole.setGroup(group);
        userGroupRole.setUser(currentUser);
        userGroupRole.setRole(UserGroupRole.Role.ADMIN);
        userGroupRoleRepository.save(userGroupRole);

        // Добавление пользователей из списка logins
        for (String login : createGroupDto.getLogins()) {
            User user = userRepository.findByLogin(login)
                    .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

            if (!userGroupRoleRepository.existsByGroupAndUser(group, user)) {
                final var newUserGroupRoleId = UserGroupRole.UserGroupRoleId.builder()
                        .groupId(group.getId())
                        .userLogin(user.getLogin())
                        .build();
                UserGroupRole newUserGroupRole = new UserGroupRole();
                newUserGroupRole.setId(newUserGroupRoleId);
                newUserGroupRole.setGroup(group);
                newUserGroupRole.setUser(user);
                newUserGroupRole.setRole(UserGroupRole.Role.USER);
                userGroupRoleRepository.save(newUserGroupRole);
            }
        }

        return new GroupDto(group.getId(), group.getName(), getUsersWithRoles(group));
    }

    @Transactional
    public GroupDto updateGroupName(Long groupId, PatchGroupDto patchGroupDto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));

        User currentUser = userService.getCurrentUser();

        if (!isAdmin(group, currentUser.getLogin())) {
            throw new AccessDeniedException("Недостаточно прав для изменения названия группы");
        }

        group.setName(patchGroupDto.getName());
        groupRepository.save(group);

        return new GroupDto(group.getId(), group.getName(), getUsersWithRoles(group));
    }

    @Transactional
    public GroupDto addUserToGroup(Long groupId, String userLogin) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));

        User currentUser = userService.getCurrentUser();

        if (!isAdmin(group, currentUser.getLogin())) {
            throw new AccessDeniedException("Недостаточно прав для добавления пользователя в группу");
        }

        User user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        // Проверяем, существует ли уже связь между группой и пользователем
        if (userGroupRoleRepository.existsByGroupAndUser(group, user)) {
            throw new UserAlreadyInGroupException("Пользователь уже состоит в группе");
        }

        // Создание новой связи с составным ключом
        final var userGroupRoleId = UserGroupRole.UserGroupRoleId.builder()
                .groupId(group.getId())
                .userLogin(user.getLogin())
                .build();
        UserGroupRole userGroupRole = new UserGroupRole();
        userGroupRole.setId(userGroupRoleId);  // Устанавливаем составной ключ
        userGroupRole.setGroup(group);
        userGroupRole.setUser(user);
        userGroupRole.setRole(UserGroupRole.Role.USER);
        userGroupRoleRepository.save(userGroupRole);

        return new GroupDto(group.getId(), group.getName(), getUsersWithRoles(group));
    }
    @Transactional
    public GroupDto removeUserFromGroup(Long groupId, String userLogin) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));

        User currentUser = userService.getCurrentUser();

        if (!isAdmin(group, currentUser.getLogin())) {
            throw new AccessDeniedException("Недостаточно прав для удаления пользователя из группы");
        }

        User user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        UserGroupRole userGroupRole = userGroupRoleRepository.findByGroupAndUser(group, user)
                .orElseThrow(() -> new UserNotInGroupException("Пользователь не состоит в группе"));

        userGroupRoleRepository.delete(userGroupRole);

        return new GroupDto(group.getId(), group.getName(), getUsersWithRoles(group));
    }

    @Transactional
    public void deleteGroup(Long groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));
        User currentUser = userService.getCurrentUser();

        if (!isAdmin(group, currentUser.getLogin())) {
            throw new AccessDeniedException("Недостаточно прав для удаления группы");
        }

        groupRepository.delete(group);
    }

    private boolean isAdmin(Group group, String userLogin) {
        return userGroupRoleRepository.existsByGroupAndUserAndRole(group, userRepository.findByLogin(userLogin).get(), UserGroupRole.Role.ADMIN);
    }

    private Set<UserGroupRoleDto> getUsersWithRoles(Group group) {
        Set<UserGroupRoleDto> userDtos = new HashSet<>();
        for (UserGroupRole userGroupRole : group.getUserGroupRoles()) {
            userDtos.add(new UserGroupRoleDto(
                    userGroupRole.getUser().getLogin(),
                    userGroupRole.getUser().getName(),
                    userGroupRole.getRole()
            ));
        }
        return userDtos;
    }

    public List<GroupInListResponse> getUserGroups() {
        final var user = userService.getCurrentUser();
        final var userGroupRoleList = userGroupRoleRepository.findAllByUser(user);
        return userGroupRoleList.stream().map(GroupInListResponse::fromUserGroupRole).toList();
    }
}




