package com.coursework.backend.test.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTestDto {
    @NotBlank
    private String name;
    private Long folderId;
    private Long groupId;
}
