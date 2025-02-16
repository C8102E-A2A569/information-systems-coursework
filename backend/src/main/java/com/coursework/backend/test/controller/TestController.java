package com.coursework.backend.test.controller;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import com.coursework.backend.folder.service.FolderService;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.group.service.GroupService;
import com.coursework.backend.test.dto.AssignTestToGroupDto;
import com.coursework.backend.test.dto.CreateTestDto;
import com.coursework.backend.test.dto.TestDto;
import com.coursework.backend.test.dto.TestRequest;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.test.service.TestService;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
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
}
