package com.coursework.backend.test.service;

import com.coursework.backend.test.dto.ResultTestResponse;
import com.coursework.backend.test.model.Results;
import com.coursework.backend.test.repository.AccessToTestsRepository;
import com.coursework.backend.test.repository.ResultsRepository;
import com.coursework.backend.test.repository.TestRepository;
import com.coursework.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResultsService {
    private final ResultsRepository resultsRepository;
//    private final TestService testService;
    private final TestRepository testRepository;
    private final UserService userService;
    private final AccessToTestsRepository accessToTestsRepository;

    public ResultTestResponse getMyTestResult(String trainingId, Long repetitionNumber) {
        final var user = userService.getCurrentUser();
        final var test = testRepository.findById(trainingId).orElseThrow(
                () -> new IllegalArgumentException("Тест не найден")
        );

        if (!accessToTestsRepository.existsByUserAndTest(user, test)) {
            throw new IllegalArgumentException("Тест недоступен для данного пользователя");
        }

        final var result = resultsRepository.findByUserAndRepetitionsCountAndTest(user, repetitionNumber, test).orElseThrow(
                () -> new IllegalArgumentException("Не удалось найти тест в пройденных у пользователя")
        );

        if (result.getStatus() == Results.Status.NOT_STARTED)
            throw new IllegalArgumentException("Пользователь ещё не завершил прохождение теста");

        return ResultTestResponse.fromTestAndResult(test, result);

    }
}
