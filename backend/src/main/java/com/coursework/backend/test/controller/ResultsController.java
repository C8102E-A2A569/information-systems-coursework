package com.coursework.backend.test.controller;

import com.coursework.backend.test.dto.ResultTestResponse;
import com.coursework.backend.test.service.ResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultsController {
    private final ResultsService resultsService;

    @GetMapping("/my/{trainingId}")
    public ResultTestResponse getMyResults(@PathVariable String trainingId) {
        return resultsService.getMyTestResult(trainingId);
    }
}
