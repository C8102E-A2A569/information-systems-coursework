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
    private Long repetitionNumber;

    public static ResultTestPreviewResponse fromResults(Results results) {
        return ResultTestPreviewResponse.builder()
                .testName(results.getTest().getName())
                .testId(results.getTest().getId())
                .userName(results.getUser().getName())
                .userLogin(results.getUser().getLogin())
                .status(results.getStatus())
                .totalPoints(results.getTotalPoints())
                .repetitionNumber(results.getRepetitionsCount())
                .build();
    }
}
