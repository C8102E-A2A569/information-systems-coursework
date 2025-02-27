package com.coursework.backend.test.service;

import com.coursework.backend.exception.exceptions.AccessDeniedException;
import com.coursework.backend.exception.exceptions.GroupNotFoundException;
import com.coursework.backend.exception.exceptions.TestNotFoundException;
import com.coursework.backend.group.repository.GroupRepository;
import com.coursework.backend.test.dto.ResultTestPreviewResponse;
import com.coursework.backend.test.dto.ResultTestResponse;
import com.coursework.backend.test.model.Results;
import com.coursework.backend.test.repository.AccessToTestsRepository;
import com.coursework.backend.test.repository.ResultsRepository;
import com.coursework.backend.test.repository.TestRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import com.coursework.backend.userGroupRole.model.UserGroupRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultsService {
    private final ResultsRepository resultsRepository;
//    private final TestService testService;
    private final TestRepository testRepository;
    private final UserService userService;
    private final AccessToTestsRepository accessToTestsRepository;
    private final GroupRepository groupRepository;

    public List<ResultTestPreviewResponse> getTestResultsInGroup(String testId, Long groupId) {
        final var currentUser = userService.getCurrentUser();
        final var group = groupRepository.findById(groupId)
                .orElseThrow(() -> new GroupNotFoundException("Группа не найдена"));

        boolean isAdmin = currentUser.getUserGroupRoles().stream()
                .anyMatch(role -> role.getGroup().getId().equals(groupId) && role.getRole() == UserGroupRole.Role.ADMIN);

        if (!isAdmin) {
            throw new AccessDeniedException("Только администратор группы может просматривать результаты тестов");
        }

        final var test = testRepository.findById(testId)
                .orElseThrow(() -> new TestNotFoundException("Тест не найден"));

        Set<User> usersInGroup = group.getUserGroupRoles().stream()
                .map(UserGroupRole::getUser)
                .collect(Collectors.toSet());

        List<ResultTestPreviewResponse> results = new ArrayList<>();

        for (User user : usersInGroup) {
            Optional<Results> resultOpt = resultsRepository.findByUserAndTestAndGroup(user, test, group);

            Results.Status status = Results.Status.NOT_STARTED;
            Double points = null;

            if (resultOpt.isPresent()) {
                Results result = resultOpt.get();
                status = result.getStatus();

                if (status == Results.Status.APPROVED) {
                    points = result.getTotalPoints();
                }
            }

            results.add(ResultTestPreviewResponse.builder()
                    .testName(test.getName())
                    .testId(test.getId())
                    .userName(user.getName())
                    .userLogin(user.getLogin())
                    .status(status)
                    .totalPoints(points)
                    .build());
        }

        return results;
    }

    public ResultTestResponse getMyTestResult(String trainingId, Long repetitionNumber) {
        final var user = userService.getCurrentUser();
        final var test = testRepository.findById(trainingId).orElseThrow(
                () -> new TestNotFoundException("Тест не найден")
        );

        if (!accessToTestsRepository.existsByUserAndTest(user, test)) {
            throw new AccessDeniedException("Тест недоступен для данного пользователя");
        }

        final var result = resultsRepository.findByUserAndRepetitionsCountAndTest(user, repetitionNumber, test).orElseThrow(
                () -> new TestNotFoundException("Не удалось найти тест в пройденных у пользователя")
        );

        if (result.getStatus() == Results.Status.NOT_STARTED)
            throw new IllegalArgumentException("Пользователь ещё не завершил прохождение теста");

        return ResultTestResponse.fromTestAndResult(test, result);

    }

}
