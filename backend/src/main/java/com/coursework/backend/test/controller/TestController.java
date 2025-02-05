package com.coursework.backend.test.controller;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.service.FolderService;
import com.coursework.backend.test.dto.TestDto;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.test.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
public class TestController {

    private final TestService testService;

    //получить список всех тестов пользователя в корневой папке автоматически при входе на сайт
    @GetMapping("/user")
    public List<TestDto> getTestsByUser() {
        return testService.getTestsByUser();
    }
}
