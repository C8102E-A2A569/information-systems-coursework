package com.coursework.backend.test.repository;

import com.coursework.backend.test.model.Results;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResultsRepository extends JpaRepository<Results, Long> {

    Optional<Results> findByUserAndTest(User user, Test test);
}
