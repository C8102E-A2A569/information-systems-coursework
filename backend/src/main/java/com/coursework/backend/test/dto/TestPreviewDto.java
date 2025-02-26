package com.coursework.backend.test.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class TestPreviewDto {
    String id;
    String name;
    Integer points;
}

