package com.coursework.backend.test.service;


import com.coursework.backend.exception.exceptions.AccessDeniedException;
import com.coursework.backend.exception.exceptions.FolderNotFoundException;
import com.coursework.backend.exception.exceptions.GroupNotFoundException;
import com.coursework.backend.exception.exceptions.TestNotFoundException;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.group.repository.GroupRepository;
import com.coursework.backend.test.dto.*;
import com.coursework.backend.test.model.AccessToTests;
import com.coursework.backend.test.model.AnswerOptions;
import com.coursework.backend.test.model.Question;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.test.repository.AccessToTestsRepository;
import com.coursework.backend.test.repository.QuestionRepository;
import com.coursework.backend.test.repository.TestRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import com.coursework.backend.userGroupRole.model.Role;
import com.coursework.backend.userGroupRole.model.UserGroupRole;
import com.coursework.backend.userGroupRole.repository.UserGroupRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestService {
    private final TestRepository testRepository;
    private final UserService userService;
    private final UserGroupRoleRepository userGroupRoleRepository;
    private final FolderRepository folderRepository;
    private final GroupRepository groupRepository;
    private final AccessToTestsRepository accessToTestsRepository;
    private final QuestionRepository questionRepository;

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

    @Transactional
    public TestDto createTest(CreateTestDto createTestDto) {
        final User currentUser = userService.getCurrentUser();

        Folder folder = null;
        if (createTestDto.getFolderId() != null) {
            folder = folderRepository.findByIdAndUser(createTestDto.getFolderId(), currentUser)
                    .orElseThrow(() -> new FolderNotFoundException("Папка не найдена"));
        }

        Group group = null;
        if (createTestDto.getGroupId() != null) {
            group = groupRepository.findById(createTestDto.getGroupId())
                    .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));

            if (!userGroupRoleRepository.existsByGroupAndUserAndRole(group, currentUser, Role.ADMIN)) {
                throw new AccessDeniedException("Только администратор группы может добавлять тесты");
            }
        }
        Test test = new Test(createTestDto.getName(), currentUser, group, folder);

        String trainingId = generateUniqueIdForField("uuid_training");
        test.setId(trainingId);

        if (createTestDto.isMonitoringMode()) {
            String monitoringId = generateUniqueIdForField("uuid_monitoring");
            test.setUuidMonitoring(monitoringId);
        }
        Test savedTest = testRepository.save(test);

        AccessToTests accessToTest = new AccessToTests();
        accessToTest.setFolder(folder);
        accessToTest.setUser(currentUser);
        accessToTest.setTest(savedTest);
        accessToTestsRepository.save(accessToTest);

        if (group != null) {
            for (UserGroupRole userGroupRole : group.getUserGroupRoles()) {
                if (!userGroupRole.getUser().equals(currentUser)) {
                    AccessToTests memberAccess = new AccessToTests();
                    memberAccess.setUser(userGroupRole.getUser());
                    memberAccess.setTest(savedTest);
                    memberAccess.setFolder(null);
                    accessToTestsRepository.save(memberAccess);
                }
            }
        }

        return savedTest.toDto();
    }
    @Transactional
    public TestDto assignTestToGroup(AssignTestToGroupDto assignTestToGroupDto) {
        final User currentUser = userService.getCurrentUser();

        Test test = testRepository.findByIdOrName(assignTestToGroupDto.getTestId(), assignTestToGroupDto.getTestId())
                .orElseThrow(() -> new TestNotFoundException("Тест не найден"));

        if (!test.getCreator().equals(currentUser)) {
            throw new AccessDeniedException("Нет прав для изменения теста");
        }

        Group group = groupRepository.findById(assignTestToGroupDto.getGroupId())
                .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));

        if (!userGroupRoleRepository.existsByGroupAndUserAndRole(group, currentUser, Role.ADMIN)) {
            throw new AccessDeniedException("Только администратор группы может добавлять тесты");
        }

        test.setGroup(group);
        Test savedTest = testRepository.save(test);

        for (UserGroupRole userGroupRole : group.getUserGroupRoles()) {
            if (!userGroupRole.getUser().equals(currentUser)) {
                if (!accessToTestsRepository.existsByUserAndTest(userGroupRole.getUser(), savedTest)) {
                    AccessToTests memberAccess = new AccessToTests();
                    memberAccess.setUser(userGroupRole.getUser());
                    memberAccess.setTest(savedTest);
                    memberAccess.setFolder(null);
                    accessToTestsRepository.save(memberAccess);
                }
            }
        }

        return savedTest.toDto();
    }
    @Transactional
    public TestDto addTestToFolder(String testId, Long folderId) {
        final User currentUser = userService.getCurrentUser();

        Test test = testRepository.findByIdOrName(testId, testId)
                .orElseThrow(() -> new TestNotFoundException("Тест не найден"));

        Folder folder = folderRepository.findByIdAndUser(folderId, currentUser)
                .orElseThrow(() -> new FolderNotFoundException("Папка не найдена"));

        boolean isMonitoringMode = test.getUuidMonitoring() != null;
        boolean isCreator = test.getCreator().equals(currentUser);
        boolean isGroupedTest = test.getGroup() != null;

        if (isGroupedTest && isMonitoringMode && !test.getCreator().equals(currentUser)) {
            throw new AccessDeniedException("Нельзя добавлять групповой тест в свою папку в режиме прохождения");
        }

        AccessToTests userAccess = accessToTestsRepository.findByUserAndTest(currentUser, test)
                .orElseGet(() -> {
                    AccessToTests newAccess = new AccessToTests();
                    newAccess.setUser(currentUser);
                    newAccess.setTest(test);
                    return newAccess;
                });

        if (isCreator) {
            if (!isMonitoringMode) {
//                test.setFolder(folder);
                testRepository.save(test);
            }
            userAccess.setFolder(folder);
            accessToTestsRepository.save(userAccess);
        } else {
            if (isMonitoringMode || test.getGroup() != null) {
                userAccess.setFolder(folder);
                accessToTestsRepository.save(userAccess);
            } else {
                throw new AccessDeniedException("Только создатель теста может добавлять тест в папку");
            }
        }

        return test.toDto();
    }


    public List<TestDto> searchTests(String searchTerm) {
        List<Test> tests = testRepository.findByIdOrNameOrUuidMonitoring(searchTerm, searchTerm, searchTerm);
        return tests.stream().map(Test::toDto).collect(Collectors.toList());
    }

    @Transactional
    public TestDto moveTestBetweenFolders(String testId, Long sourceFolderId, Long targetFolderId) {
        final User currentUser = userService.getCurrentUser();

        Test test = testRepository.findByIdOrName(testId, testId)
                .orElseThrow(() -> new TestNotFoundException("Тест не найден"));

        Folder sourceFolder = folderRepository.findByIdAndUser(sourceFolderId, currentUser)
                .orElseThrow(() -> new FolderNotFoundException("Исходная папка не найдена"));

        Folder targetFolder = folderRepository.findByIdAndUser(targetFolderId, currentUser)
                .orElseThrow(() -> new FolderNotFoundException("Целевая папка не найдена"));

        AccessToTests userAccess = accessToTestsRepository.findAllByUserAndFolder(currentUser, sourceFolder)
                .stream()
                .filter(access -> access.getTest().equals(test))
                .findFirst()
                .orElseThrow(() -> new AccessDeniedException("У вас нет доступа к данному тесту в указанной папке"));

        boolean isMonitoringMode = test.getUuidMonitoring() != null;
        boolean isCreator = test.getCreator().equals(currentUser);

        if (isCreator) {
            if (!isMonitoringMode) {
//                test.setFolder(targetFolder);
                testRepository.save(test);
            }
            userAccess.setFolder(targetFolder);
            accessToTestsRepository.save(userAccess);
        } else {
            if (isMonitoringMode || test.getGroup() != null) {
                userAccess.setFolder(targetFolder);
                accessToTestsRepository.save(userAccess);
            } else {
                throw new AccessDeniedException("Только создатель теста может перемещать тест между папками");
            }
        }

        return test.toDto();
    }

    @Transactional
    public void deleteTest(String testId) {
        final User currentUser = userService.getCurrentUser();

        Test test = testRepository.findByIdOrName(testId, testId)
                .orElseThrow(() -> new TestNotFoundException("Тест не найден"));

        if (!test.getCreator().equals(currentUser)) {
            throw new AccessDeniedException("Только создатель теста может его удалить");
        }

        List<AccessToTests> accessRecords = accessToTestsRepository.findAllByTest(test);
        accessToTestsRepository.deleteAll(accessRecords);

        testRepository.delete(test);
    }

    public void generateMonitoringIdForTest(String testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new IllegalArgumentException("Тест не найден"));

        String uniqueMonitoringId = generateUniqueIdForField("uuid_monitoring");
        test.setUuidMonitoring(uniqueMonitoringId);
        testRepository.save(test);
    }

//    TODO: Можно добавить более подходящие ошибки
    public TestForTrainingRequest getTestForTraining(String id) {
        final var currentUser = userService.getCurrentUser();
        final var test = testRepository.findById(id).orElseThrow(() ->
                new IllegalArgumentException("Не удалось найти заданный тест")
        );
        if (!accessToTestsRepository.existsByUserAndTest(currentUser, test)) {
            throw new IllegalArgumentException("Данный пользователь не имеет доступа для прохождения заданного теста");
        }

        return TestForTrainingRequest.fromTest(test);
    }

//    TODO: Изменить возвращаемый тип
    public void checkTrainingResult(TestForCheck testForCheck) {
        final var currentUser = userService.getCurrentUser();
        final var test = testRepository.findById(testForCheck.getId()).orElseThrow(() ->
                new IllegalArgumentException("Заданного теста не найдено"));
//        Проверка на количество ответов
        final var questionCountDifference = testForCheck.getQuestionsForCheck().size() - test.getQuestions().size();
        if (questionCountDifference < 0)
            throw new IllegalArgumentException("Были даны ответы не на все вопросы");
        if (questionCountDifference > 0)
            throw new IllegalArgumentException("Пришло больше ответов, чем ожидалось");
//        Проверка на уникальность вопросов
        Set<Long> questionIdsSet = new HashSet<>();
        final var testPoints = test.getPoints();
        Integer questionsPointsSum = 0;
        Integer correctQuestionsPointsSum = 0;
        Boolean isContainsTextAnswers = false;
        for (final var questionForCheck : testForCheck.getQuestionsForCheck()) {
//            Проверка, были ли уже дан ответ на данный вопрос
            if (questionIdsSet.contains(questionForCheck.getId()))
                throw new IllegalArgumentException("Ответ на данный вопрос уже был дан");
            questionIdsSet.add(questionForCheck.getId());

            final var currentQuestion = questionRepository.findById(questionForCheck.getId()).orElseThrow(
                    () -> new IllegalArgumentException("Не удалось идентифицировать вопрос")
            );

            if (currentQuestion.getTest().getId() != testForCheck.getId())
                throw new IllegalArgumentException("Вопрос не принадлежит данному тесту");

//            TODO: Добавить проверку на доступ к тесту
            questionsPointsSum += currentQuestion.getPoints();
            switch (currentQuestion.getType()) {
                case TEXT:
                    isContainsTextAnswers = true;
                    break;
                case CHECKBOX:
                    final var correctOptions = currentQuestion.getAnswerOptions().stream()
                            .filter(AnswerOptions::getIsCorrect)
                            .collect(Collectors.toSet());
                    break;
                case RADIOBUTTON:
                    final var correctOption = currentQuestion.getAnswerOptions()
                            .stream()
                            .filter(AnswerOptions::getIsCorrect)
                            .findFirst().orElseThrow(
                                    () -> new IllegalArgumentException("Не удалось найти правильный ответ"));
                    if (Objects.equals(correctOption.getId(), questionForCheck.getId()))
                        correctQuestionsPointsSum += currentQuestion.getPoints();
                    break;
            }
        }
    }

    private String generateUniqueIdForField(String fieldName) {
        String generatedId;
        do {
            generatedId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 8);
        } while (testRepository.existsByField(fieldName, generatedId));

        return generatedId;
    }
}
