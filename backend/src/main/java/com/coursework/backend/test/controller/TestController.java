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
    @GetMapping("/{testId}")
    public TestDto getTestById(@PathVariable String testId) {
        return testService.getTestById(testId);
    }
    @GetMapping("/user")
    public List<TestPreviewDto> getTestsByUser() {
        return testService.getRootTestsByUser();
    }

    @PostMapping("/folder")
    public List<TestPreviewDto> getTestsByUserAndFolder(@RequestBody TestRequest testRequest) {
        return testService.getTestsByUserAndFolder(testRequest.getFolderId());
    }

    @GetMapping("/search")
    public List<TestPreviewDto> searchTests(@RequestParam String query) {
        return testService.searchTests(query);
    }

    @Deprecated
    @PostMapping("/create")
    public TestPreviewDto createTest(@Valid @RequestBody CreateTestDto createTestDto) {
        return testService.createTest(createTestDto);
    }

    @PostMapping("/create/new")
    public TestPreviewDto create(@RequestBody TestDto testDto, @RequestParam(defaultValue = "") Long folderId) {
        return testService.create(testDto, folderId);
    }

    @PostMapping("/assign-group")
    public TestPreviewDto assignTestToGroup(@Valid @RequestBody AssignTestToGroupDto assignTestToGroupDto) {
        return testService.assignTestToGroup(assignTestToGroupDto);
    }

    @PostMapping("/add-to-folder")
    public TestPreviewDto addTestToFolder(@RequestParam String testId, @RequestParam Long folderId) {
        return testService.addTestToFolder(testId, folderId);
    }

    @PostMapping("/move")
    public TestPreviewDto moveTest(@RequestParam String testId,
                                   @RequestParam Long sourceFolderId,
                                   @RequestParam Long targetFolderId) {
        return testService.moveTestBetweenFolders(testId, sourceFolderId, targetFolderId);
    }

    @DeleteMapping("/delete/{testId}")
    public void deleteTest(@PathVariable String testId) {
        testService.deleteTest(testId);
    }

    @GetMapping("/training/{trainingId}")
    public TestDto getTestForTraining(@PathVariable String trainingId) {
        return testService.getTestForTraining(trainingId);
    }

    @PostMapping("training/send")
    public void checkTrainingResult(@RequestBody TestForCheck testForCheck) {
        testService.checkTrainingResult(testForCheck);
    }

    @GetMapping("training/search/{trainingId}")
    public TestPreviewDto searchTrainingTest(@PathVariable String trainingId) {
        return testService.searchTrainingTest(trainingId);
    }

    @GetMapping("/{groupId}/tests")
    public List<GroupTestDto> getGroupTests(@PathVariable Long groupId) {
        return testService.getGroupTests(groupId);
    }

}
