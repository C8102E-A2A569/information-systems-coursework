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
public class TestForTrainingResponse {
    private String id;

    private String name;

    private String creatorName;

    private Integer points;

    private List<QuestionForTrainingResponse> questions;

    public static TestForTrainingResponse fromTest(Test test) {
        final var testCreatorName = test.getCreator().getName();
        final var testQuestions = test.getQuestions() != null
                ? test.getQuestions().stream().map(QuestionForTrainingResponse::fromQuestion).toList()
                : null;
        return TestForTrainingResponse.builder()
                .id(test.getId())
                .name(test.getName())
                .points(test.getPoints())
                .creatorName(testCreatorName)
                .questions(testQuestions)
                .build();
    }
}
