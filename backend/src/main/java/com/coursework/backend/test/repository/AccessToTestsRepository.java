package com.coursework.backend.test.repository;

import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.test.model.AccessToTests;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AccessToTestsRepository extends JpaRepository<AccessToTests, String> {
    List<AccessToTests> findAllByUserAndFolder(User user, Folder folder);
    boolean existsByUserAndTest(User user, Test test);
    List<AccessToTests> findAllByTest(Test test);

    Optional<AccessToTests> findByUserAndTest(User user, Test test);

}