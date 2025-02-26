package com.coursework.backend.test.controller;

import com.coursework.backend.test.dto.*;
import com.coursework.backend.test.service.TestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    //получить список всех тестов пользователя в корневой папке автоматически при входе на сайт
    @GetMapping("/user")
    public List<TestDto> getTestsByUser() {
        return testService.getRootTestsByUser();
    }

    @PostMapping("/folder")
    public List<TestDto> getTestsByUserAndFolder(@RequestBody TestRequest testRequest) {
        return testService.getTestsByUserAndFolder(testRequest.getFolderId());
    }

    @GetMapping("/search")
    public List<TestDto> searchTests(@RequestParam String query) {
        return testService.searchTests(query);
    }

    @PostMapping("/create")
    public TestDto createTest(@Valid @RequestBody CreateTestDto createTestDto) {
        return testService.createTest(createTestDto);
    }

    @PostMapping("/assign-group")
    public TestDto assignTestToGroup(@Valid @RequestBody AssignTestToGroupDto assignTestToGroupDto) {
        return testService.assignTestToGroup(assignTestToGroupDto);
    }

    @PostMapping("/add-to-folder")
    public TestDto addTestToFolder(@RequestParam String testId, @RequestParam Long folderId) {
        return testService.addTestToFolder(testId, folderId);
    }

    @PostMapping("/move")
    public TestDto moveTest(@RequestParam String testId,
                            @RequestParam Long sourceFolderId,
                            @RequestParam Long targetFolderId) {
        return testService.moveTestBetweenFolders(testId, sourceFolderId, targetFolderId);
    }

    @DeleteMapping("/delete/{testId}")
    public void deleteTest(@PathVariable String testId) {
        testService.deleteTest(testId);
    }

    @GetMapping("/training/{trainingId}")
    public TestForTrainingResponse getTestForTraining(@PathVariable String trainingId) {
        return testService.getTestForTraining(trainingId);
    }

    @PostMapping("training/send")
    public void checkTrainingResult(@RequestBody TestForCheck testForCheck) {
        testService.checkTrainingResult(testForCheck);
    }

    @GetMapping("training/search/{trainingId}")
    public TestDto searchTrainingTest(@PathVariable String trainingId) {
        return testService.searchTrainingTest(trainingId);
    }
}
