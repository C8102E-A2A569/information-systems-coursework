package com.coursework.backend.userGroupRole.repository;

import com.coursework.backend.userGroupRole.model.UserGroupRole;
import com.coursework.backend.userGroupRole.model.UserGroupRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserGroupRoleRepository extends JpaRepository<UserGroupRole, UserGroupRoleId> {
    Optional<UserGroupRole> findById_UserLoginAndId_GroupId(String userLogin, Long groupId);

    boolean existsById_UserLoginAndId_GroupId(String userLogin, Long groupId);
}
