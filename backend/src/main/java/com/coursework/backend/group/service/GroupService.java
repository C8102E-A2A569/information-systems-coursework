package com.coursework.backend.group.service;

import com.coursework.backend.exception.exceptions.AccessDeniedException;
import com.coursework.backend.exception.exceptions.GroupNotFoundException;
import com.coursework.backend.exception.exceptions.UserNotFoundException;
import com.coursework.backend.exception.exceptions.UserNotInGroupException;
import com.coursework.backend.group.dto.CreateGroupDto;
import com.coursework.backend.group.dto.GroupDto;
import com.coursework.backend.group.dto.PatchGroupDto;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.group.repository.GroupRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import com.coursework.backend.userGroupRole.model.Role;
import com.coursework.backend.userGroupRole.model.UserGroupRole;
import com.coursework.backend.userGroupRole.model.UserGroupRoleId;
import com.coursework.backend.userGroupRole.service.UserGroupRoleService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserGroupRoleService userGroupRoleService;

//    TODO: тут возникает ошибка
    @Transactional
    public GroupDto createGroup(String login, CreateGroupDto dto) {
        // Найдем пользователя
        User admin = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с логином %s не найден", login)));

        // Создаем группу
        Group group = new Group();
        group.setName(dto.getName());
        group = groupRepository.save(group); // Сохраняем и получаем ID

        // Добавляем пользователя в группу
        group.getUsers().add(admin);
        admin.getGroups().add(group); // Обновляем обе стороны связи

        // Сохраняем изменения
        groupRepository.save(group); // Повторно сохраняем, чтобы обновить связь
        userRepository.save(admin);

        // Создаем роль администратора
        UserGroupRole userGroupRole = new UserGroupRole();
        userGroupRole.setGroup(group); // Используем сохраненную группу с ID
        userGroupRole.setUser(admin);
        userGroupRole.setRole(Role.ADMIN);

        // Создаем и сохраняем UserGroupRole с использованием ID
        userGroupRoleService.save(userGroupRole);

        return new GroupDto(group.getId(), group.getName(), group.getUsers().stream()
                .map(User::getLogin).collect(Collectors.toSet()));
    }


    public void deleteGroup(Long groupId, String userLogin) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(String.format("Группа c id %s не найдена", groupId)));

        Role role = userGroupRoleService.getUserRoleInGroup(userLogin, groupId);
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Только администратор может удалить группу");
        }
        groupRepository.delete(group);
        simpMessagingTemplate.convertAndSend("/topic", "Группа удалена");
    }

    public void addUserToGroup(Long groupId, String userLogin) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(String.format("Группа c id %s не найдена", groupId)));

        Role role = userGroupRoleService.getUserRoleInGroup(userLogin, groupId);
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Только администратор может добавить пользователя в группу");
        }

        User user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с логином %s не найден", userLogin)));
        group.getUsers().add(user);
        groupRepository.save(group);
        simpMessagingTemplate.convertAndSend("/topic", "Пользователь добавлен в группу");
    }

    public void removeUserFromGroup(Long groupId, String userLogin) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(String.format("Группа c id %s не найдена", groupId)));

        Role role = userGroupRoleService.getUserRoleInGroup(userLogin, groupId);
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Только администратор может удалить пользователя из группы");
        }

        User user = userRepository.findByLogin(userLogin)
                .orElseThrow(() -> new UserNotFoundException(String.format("Пользователь с логином %s не найден", userLogin)));
        if (!group.getUsers().remove(user)) {
            throw new UserNotInGroupException(String.format("Пользователь с логином %s не состоит в группе", userLogin));
        }
        groupRepository.save(group);
        simpMessagingTemplate.convertAndSend("/topic", "Пользователь удален из группы");
    }

    public GroupDto updateGroupName(Long groupId, String userLogin, PatchGroupDto dto) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException(String.format("Группа с id %s не найдена", groupId)));

        Role role = userGroupRoleService.getUserRoleInGroup(userLogin, groupId);
        if (role != Role.ADMIN) {
            throw new AccessDeniedException("Только администратор может изменять имя группы");
        }

        group.setName(dto.getName());
        groupRepository.save(group);

        return new GroupDto(group.getId(), group.getName(), group.getUsers().stream()
                .map(User::getLogin).collect(Collectors.toSet()));
    }
}

