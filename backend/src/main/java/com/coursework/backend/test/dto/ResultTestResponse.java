package com.coursework.backend.test.dto;

import com.coursework.backend.test.model.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultTestResponse {
    private String trainingId;

    private Long resultId;

    private String name;

    private LocalDateTime startTime;

    private LocalDateTime testTime;

    private String creatorName;

    private Results.Status status;

    private Integer testTotalPoints;

    private Double testResultPoints;

    private List<QuestionResult> questions;

    public static ResultTestResponse fromTestAndResult(Test test, Results result) {
        final var questions = result.getAnswers()
                .stream()
                .map((answer) -> QuestionResult.fromQuestionAndAnswer(answer.getQuestion(), answer))
                .toList();

        return ResultTestResponse.builder()
                .trainingId(test.getId())
                .resultId(result.getId())
                .name(test.getName())
                .startTime(result.getStartTime())
                .testTime(result.getTestTime())
                .creatorName(test.getCreator().getName())
                .status(result.getStatus())
                .testTotalPoints(test.getPoints())
                .testResultPoints(result.getTotalPoints())
                .questions(questions)
                .build();

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QuestionResult {
        private Long questionId;

        private Long answerId;

        private Question.Type type;

        private Integer questionTotalPoints;

        private Integer questionResultPoints;

        private String question;

        private List<AnswerOptionResult> results;

//        TODO: исправить, чтобы возвращал текстовый ответ
        public static QuestionResult fromQuestionAndAnswer(Question question, Answers answer) {
            final var results = question.getAnswerOptions()
                    .stream()
                    .map((option) -> AnswerOptionResult.fromAnswerOptionsAndAnswer(option, answer))
                    .toList();

            return QuestionResult.builder()
                    .questionId(question.getId())
                    .answerId(answer.getId())
                    .type(question.getType())
                    .questionTotalPoints(question.getPoints())
                    .questionResultPoints(answer.getPoints())
                    .question(question.getQuestion())
                    .results(results)
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AnswerOptionResult {
        private String option;

        private Boolean isCorrect;

        private Boolean isAnsweredByUser;

        public static AnswerOptionResult fromAnswerOptionsAndAnswer(AnswerOptions option, Answers answer) {
            final var isAnsweredByUser = Arrays.stream(answer.getUserAnswer().split(";"))
                    .toList().contains(option.getId().toString());
            return AnswerOptionResult.builder()
                    .option(option.getOption())
                    .isCorrect(option.getIsCorrect())
                    .isAnsweredByUser(isAnsweredByUser)
                    .build();
        }
    }
}
