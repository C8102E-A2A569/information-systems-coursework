package com.coursework.backend.test.dto;

import com.coursework.backend.test.model.Results;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultTestPreviewResponse {
    private String testName;
    private String testId;
    private String userName;
    private String userLogin;
    private Results.Status status;
    private Double totalPoints;
}
