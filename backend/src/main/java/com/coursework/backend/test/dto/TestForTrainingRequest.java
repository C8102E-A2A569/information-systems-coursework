package com.coursework.backend.test.dto;

import com.coursework.backend.test.model.Test;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestForTrainingRequest {
    private String id;

    private String name;

    private String creatorName;

    private Integer points;

    private List<QuestionForTrainingRequest> questions;

    public static TestForTrainingRequest fromTest(Test test) {
        final var testCreatorName = test.getCreator().getName();
        final var testQuestions = test.getQuestions() != null
                ? test.getQuestions().stream().map(QuestionForTrainingRequest::fromQuestion).toList()
                : null;
        return TestForTrainingRequest.builder()
                .id(test.getId())
                .name(test.getName())
                .points(test.getPoints())
                .creatorName(testCreatorName)
                .questions(testQuestions)
                .build();
    }
}
