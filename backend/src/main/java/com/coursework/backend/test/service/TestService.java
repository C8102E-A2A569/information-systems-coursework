package com.coursework.backend.test.service;


import com.coursework.backend.exception.exceptions.AccessDeniedException;
import com.coursework.backend.exception.exceptions.FolderNotFoundException;
import com.coursework.backend.exception.exceptions.GroupNotFoundException;
import com.coursework.backend.exception.exceptions.TestNotFoundException;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.group.repository.GroupRepository;
import com.coursework.backend.test.dto.AssignTestToGroupDto;
import com.coursework.backend.test.dto.CreateTestDto;
import com.coursework.backend.test.dto.TestDto;
import com.coursework.backend.test.model.AccessToTests;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.test.repository.AccessToTestsRepository;
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
    private final GroupRepository groupRepository;
    private final AccessToTestsRepository accessToTestsRepository;

    public List<TestDto> getRootTestsByUser() {
        final User user = userService.getCurrentUser();
        List<AccessToTests> accessToTests = accessToTestsRepository.findAllByUserAndFolder(user, null);

        return accessToTests.stream()
                .map((access) -> access.getTest().toDto())
                .collect(Collectors.toList());
    }

    public List<TestDto> getTestsByUserAndFolder(Long folderId) {
        final User user = userService.getCurrentUser();
        final var folder = folderRepository.findByIdAndUser(folderId, user)
                .orElseThrow(() -> new RuntimeException("Папка не найдена"));
        final var tests = accessToTestsRepository.findAllByUserAndFolder(user, folder);
        return tests.stream()
                .map((access) -> access.getTest().toDto())
                .collect(Collectors.toList());
    }


    public TestDto createTest(CreateTestDto createTestDto) {
        final User currentUser = userService.getCurrentUser();

        // Get folder if specified
        Folder folder = null;
        if (createTestDto.getFolderId() != null) {
            folder = folderRepository.findByIdAndUser(createTestDto.getFolderId(), currentUser)
                    .orElseThrow(() -> new FolderNotFoundException("Папка не найдена"));
        }

        // Get group if specified
        Group group = null;
        if (createTestDto.getGroupId() != null) {
            group = groupRepository.findById(createTestDto.getGroupId())
                    .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));
        }

        Test test = new Test(createTestDto.getName(), currentUser, group, folder);
        String uniqueId = generateUniqueIdForField("uuid_training");
        test.setId(uniqueId);

        Test savedTest = testRepository.save(test);
        //не проверено
        AccessToTests accessToTest = new AccessToTests();
        accessToTest.setFolder(folder);
        accessToTest.setUser(currentUser);
        accessToTest.setTest(test);
        accessToTestsRepository.save(accessToTest);

        return savedTest.toDto();
    }

    // тут тоже надо дописать, что тест появляется у пользователей в какой-то папке в таблице access_to_tests
    public TestDto assignTestToGroup(AssignTestToGroupDto assignTestToGroupDto) {
        final User currentUser = userService.getCurrentUser();

        Test test = testRepository.findById(assignTestToGroupDto.getTestId())
                .orElseThrow(() -> new TestNotFoundException("Тест не найден"));

        // Verify test ownership
        if (!test.getCreator().equals(currentUser)) {
            throw new AccessDeniedException("Нет прав для изменения теста");
        }

        Group group = groupRepository.findById(assignTestToGroupDto.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));

        test.setGroup(group);
        Test savedTest = testRepository.save(test);
        return savedTest.toDto();
    }

    public List<TestDto> searchTests(String searchTerm) {
        List<Test> tests = testRepository.findByIdOrNameOrUuidMonitoring(searchTerm, searchTerm, searchTerm);
        return tests.stream().map(Test::toDto).collect(Collectors.toList());
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
