package com.coursework.backend.test.controller;

import com.coursework.backend.test.dto.ResultTestPreviewResponse;
import com.coursework.backend.test.dto.ResultTestResponse;
import com.coursework.backend.test.service.ResultsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/results")
@RequiredArgsConstructor
public class ResultsController {
    private final ResultsService resultsService;

    @GetMapping("/my/{trainingId}/{repetitionNumber}")
    public ResultTestResponse getMyResults(@PathVariable String trainingId, @PathVariable Long repetitionNumber) {
        return resultsService.getMyTestResult(trainingId, repetitionNumber);
    }
    @GetMapping("/group/{groupId}/test/{testId}")
    public List<ResultTestPreviewResponse> getTestResultsInGroup(
            @PathVariable Long groupId,
            @PathVariable String testId) {
        return resultsService.getTestResultsInGroup(testId, groupId);
    }
}
