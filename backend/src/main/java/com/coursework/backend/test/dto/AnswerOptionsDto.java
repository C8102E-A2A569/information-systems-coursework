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
public class AnswerOptionsDto {
    private Long id;

    private String option;

    public static AnswerOptionsDto fromAnswerOptions(AnswerOptions options) {
        return AnswerOptionsDto.builder()
                .id(options.getId())
                .option(options.getOption())
                .build();
    }
}
