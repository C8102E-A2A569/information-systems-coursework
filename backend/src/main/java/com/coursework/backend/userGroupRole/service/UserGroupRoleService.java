package com.coursework.backend.userGroupRole.service;


import com.coursework.backend.exception.exceptions.*;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.group.repository.GroupRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import com.coursework.backend.userGroupRole.model.UserGroupRole;
import com.coursework.backend.userGroupRole.repository.UserGroupRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserGroupRoleService {

    private final UserGroupRoleRepository userGroupRoleRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    public void save(UserGroupRole userGroupRole) {
        userGroupRoleRepository.save(userGroupRole);
    }
}
