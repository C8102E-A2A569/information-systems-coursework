package com.coursework.backend.test.repository;

import com.coursework.backend.test.model.AnswerOptions;
import com.coursework.backend.test.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerOptionsRepository extends JpaRepository<AnswerOptions, Long> {
}
