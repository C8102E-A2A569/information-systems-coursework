package com.coursework.backend.userGroupRole.repository;

import com.coursework.backend.group.model.Group;
import com.coursework.backend.user.model.User;
import com.coursework.backend.userGroupRole.model.Role;
import com.coursework.backend.userGroupRole.model.UserGroupRole;
import com.coursework.backend.userGroupRole.model.UserGroupRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, Long> {
    boolean existsByGroupAndUser(Group group, User user);
    boolean existsByGroupAndUserAndRole(Group group, User user, Role role);
    Optional<UserGroupRole> findByGroupAndUser(Group group, User user);
}
