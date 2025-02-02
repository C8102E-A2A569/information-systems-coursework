package com.coursework.backend.entity;

import com.coursework.backend.dto.FolderDto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Folder")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_login", referencedColumnName = "login", insertable = false, updatable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Folder parentFolder;


    public FolderDto toDto(){
        return FolderDto.builder()
                .id(this.id)
                .name(this.name)
                .parentFolderId(this.parentFolder != null ? this.parentFolder.id : null)
                .userLogin(this.user.getLogin())
                .build();
    }
}
