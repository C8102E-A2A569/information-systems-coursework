package com.coursework.backend.test.service;


import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.test.dto.TestDto;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.test.repository.TestRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final UserService userService;
    private final FolderRepository folderRepository;


    public List<TestDto> getTestsByUser() {
        final User user = userService.getCurrentUser();
        List<Test> tests = testRepository.findAllByCreatorAndFolder(user, null);
        return tests.stream().map(Test::toDto).collect(Collectors.toList());
    }

    public List<TestDto> getTestsByUserAndFolder(Long folderId) {
        final User user = userService.getCurrentUser();
        final var folder = folderRepository.findByIdAndUser(folderId, user)
                .orElseThrow(() -> new RuntimeException("Папка не найдена"));
        final var tests = testRepository.findAllByCreatorAndFolder(user, folder);
        return tests.stream()
                .map(Test::toDto)
                .toList();
    }

    public Test createTest(String name, User creator, Group group, Folder folder) {
        Test test = new Test(name, creator, group, folder);

        String uniqueId = generateUniqueIdForField("uuid_training");
        test.setId(uniqueId);
        return testRepository.save(test);
    }

    public void generateMonitoringIdForTest(String testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Тест не найден"));

        String uniqueMonitoringId = generateUniqueIdForField("uuid_monitoring");
        test.setUuidMonitoring(uniqueMonitoringId);
        testRepository.save(test);
    }

    private String generateUniqueIdForField(String fieldName) {
        String generatedId;
        do {
            generatedId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        } while (testRepository.existsByField(fieldName, generatedId));

        return generatedId;
    }

}
