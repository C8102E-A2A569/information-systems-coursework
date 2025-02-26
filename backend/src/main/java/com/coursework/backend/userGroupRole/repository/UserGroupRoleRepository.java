package com.coursework.backend.userGroupRole.repository;

import com.coursework.backend.group.model.Group;
import com.coursework.backend.user.model.User;

import com.coursework.backend.userGroupRole.model.UserGroupRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, Long> {
    boolean existsByGroupAndUser(Group group, User user);
    boolean existsByGroupAndUserAndRole(Group group, User user, UserGroupRole.Role role);
    Optional<UserGroupRole> findByGroupAndUser(Group group, User user);

    List<UserGroupRole> findAllByUser(User user);
}
