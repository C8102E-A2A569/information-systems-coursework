package com.coursework.backend.test.dto;

import com.coursework.backend.test.model.Question;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestForCheck {
    private String id;

    private List<QuestionForCheck> questionsForCheck;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionForCheck {
        private Long id;

        private String textAnswer;

        private List<Long> checkboxAnswersId;

        private Long radiobuttonAnswerId;
    }
}
