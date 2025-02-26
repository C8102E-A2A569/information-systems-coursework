package com.coursework.backend.userGroupRole.model;

import com.coursework.backend.group.model.Group;
import com.coursework.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_group_roles")
public class UserGroupRole {

    @EmbeddedId
    private UserGroupRoleId id;

    @ManyToOne
    @MapsId("groupId")
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @MapsId("userLogin")
    @JoinColumn(name = "user_login")
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    public enum Role {
        ADMIN,
        USER
    }

    @Data
    @Builder
    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserGroupRoleId {
        private Long groupId;
        private String userLogin;
    }
}
