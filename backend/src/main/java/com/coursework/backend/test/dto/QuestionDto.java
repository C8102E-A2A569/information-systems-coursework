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
public class QuestionDto {
    private Long id;

    private String question;

    private Integer points;

    private Question.Type type;

    private List<AnswerOptionsDto> answerOptions;

    public static QuestionDto fromQuestion(Question question) {
        final var answerOptions = question.getAnswerOptions() != null
                ? question.getAnswerOptions().stream().map(AnswerOptionsDto::fromAnswerOptions).toList()
                : null;
        return QuestionDto.builder()
                .id(question.getId())
                .question(question.getQuestion())
                .points(question.getPoints())
                .type(question.getType())
                .answerOptions(answerOptions)
                .build();
    }
}
