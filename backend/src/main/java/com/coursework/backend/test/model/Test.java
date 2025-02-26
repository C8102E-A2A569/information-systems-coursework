package com.coursework.backend.test.model;

import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.test.dto.TestDto;
import com.coursework.backend.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name="tests")
public class Test {

    @Id
    @Column(name = "uuid_training", nullable = false, length = 8)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_login", referencedColumnName = "login", nullable = false)
    private User creator;

    @Column(name = "uuid_monitoring", length = 8)
    private String uuidMonitoring;

    @Column(name = "points")
    private Integer points;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    @OneToMany(mappedBy = "test", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Question> questions;

/*
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "folder_id", referencedColumnName = "id")
    private Folder folder;
*/

    public Test(String name, User creator, Group group, Folder folder) {
        this.creator = creator;
        this.name = name;
        this.group = group;
//        this.folder = folder;
    }

    @PrePersist
    protected void onCreate() {
        if (id == null || id.isEmpty()) {
            setId(generateUniqueId(8));
        }
    }
    public void generateMonitoringId() {
        this.uuidMonitoring = generateUniqueId(8);
    }

    private String generateUniqueId(int length) {
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        return uuid.substring(0, Math.min(length, uuid.length()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Test test = (Test) o;
        return Objects.equals(id, test.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public TestDto toDto() {
        return TestDto.builder()
                .id(this.id)
                .name(this.name)
                .points(this.points)
//                .groupId(this.group  != null ? this.group.getId() : null)
//                .folderId(this.folder != null ? this.folder.getId() : null)
                .build();
    }
}
