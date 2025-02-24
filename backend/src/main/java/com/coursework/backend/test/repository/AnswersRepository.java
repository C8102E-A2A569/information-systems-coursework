package com.coursework.backend.test.repository;

import com.coursework.backend.test.model.Answers;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswersRepository extends JpaRepository<Answers, Long> { }
