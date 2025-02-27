package com.coursework.backend.test.repository;

import com.coursework.backend.group.model.Group;
import com.coursework.backend.test.model.Results;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ResultsRepository extends JpaRepository<Results, Long> {

    List<Results> findByUserAndTest(User user, Test test);

    @Query(value = "SELECT * FROM results r " +
            "WHERE r.user_login = :userLogin AND r.test_id = :testId " +
            "ORDER BY r.repetitions_count DESC NULLS LAST " +
            "LIMIT 1", nativeQuery = true)
    Optional<Results> findLastByUserLoginAndTestId(@Param("userLogin") String userLogin, @Param("testId") String testId);

    Optional<Results> findByUserAndTestAndGroup(User user, Test test, Group group);

    Optional<Results> findByUserAndRepetitionsCountAndTest(User user, Long repetitionsCount, Test test);

    List<Results> findAllByUser(User user);
}
