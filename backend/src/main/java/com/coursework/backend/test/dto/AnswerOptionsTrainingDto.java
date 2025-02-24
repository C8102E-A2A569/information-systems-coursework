package com.coursework.backend.test.dto;

import com.coursework.backend.test.model.AnswerOptions;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerOptionsTrainingDto {
    private Long id;

    private String option;

    public static AnswerOptionsTrainingDto fromAnswerOptions(AnswerOptions options) {
        return AnswerOptionsTrainingDto.builder()
                .id(options.getId())
                .option(options.getOption())
                .build();
    }
}
