package com.coursework.backend.userGroupRole.model;

import com.coursework.backend.group.model.Group;
import com.coursework.backend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_group_roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
