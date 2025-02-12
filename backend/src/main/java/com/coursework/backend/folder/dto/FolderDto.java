package com.coursework.backend.folder.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class FolderDto {
    Long id;
    String name;
    String userLogin;
    Long parentFolderId;
}