package com.coursework.backend.folder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateFolderRequest {
    private String name;
    private Long parentFolderId;
}