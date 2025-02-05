package com.coursework.backend.userGroupRole.service;


import com.coursework.backend.exception.exceptions.UserGroupRoleNotFoundException;
import com.coursework.backend.exception.exceptions.UserNotFoundException;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import com.coursework.backend.userGroupRole.model.Role;
import com.coursework.backend.userGroupRole.model.UserGroupRole;
import com.coursework.backend.userGroupRole.repository.UserGroupRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupRoleService {
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final UserRepository userRepository;

    public Role getUserRoleInGroup(String login, Long groupId) {
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с логином %s не найден"));

        UserGroupRole userGroupRole = userGroupRoleRepository.findById_UserLoginAndId_GroupId(login, groupId)
                .orElseThrow(() -> new UserGroupRoleNotFoundException("Роль пользователя с логином %s в группе с id %s не найдена", login, groupId));

        return userGroupRole.getRole();
    }

    public void save(UserGroupRole userGroupRole) {
        userGroupRoleRepository.save(userGroupRole);
    }
}
