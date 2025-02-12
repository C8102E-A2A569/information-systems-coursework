package com.coursework.backend.folder.model;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.dto.FolderDtoRequest;
import com.coursework.backend.user.model.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "folders")
public class Folder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_login", referencedColumnName = "login")
    private User user;

    @ManyToOne
    @JoinColumn(name = "parent_folder_id", referencedColumnName = "id")
    private Folder parentFolder;

    @OneToMany(mappedBy = "parentFolder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Folder> subFolders = new ArrayList<>();

    public FolderDto toDto(){
        return FolderDto.builder()
                .id(this.id)
                .name(this.name)
                .parentFolderId(this.parentFolder != null ? this.parentFolder.id : null)
                .userLogin(this.user.getLogin())
                .build();
    }
    public Folder dtoRequestToFolder(FolderDtoRequest request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("Id папки не указан");
        }
        return Folder.builder()
                .id(request.getId())
                .build();
    }
}