package com.coursework.backend.test.repository;

import com.coursework.backend.test.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
