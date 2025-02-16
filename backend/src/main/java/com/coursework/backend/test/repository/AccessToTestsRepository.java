package com.coursework.backend.test.repository;

import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.test.model.AccessToTests;
import com.coursework.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccessToTestsRepository extends JpaRepository<AccessToTests, String> {
    List<AccessToTests> findAllByUserAndFolder(User user, Folder folder);
}
