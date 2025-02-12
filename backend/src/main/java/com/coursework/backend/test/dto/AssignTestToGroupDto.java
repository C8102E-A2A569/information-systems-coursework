package com.coursework.backend.test.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignTestToGroupDto {
    @NotNull
    private String testId;
    @NotNull
    private Long groupId;
}
