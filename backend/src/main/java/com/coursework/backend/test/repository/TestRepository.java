package com.coursework.backend.test.repository;

import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TestRepository extends JpaRepository<Test, String> {
    boolean existsById(String id);
    List<Test> findAllByCreatorAndFolder(User user, Folder folder);
    boolean existsByUuidMonitoring(String uuidMonitoring);

    default boolean existsByField(String fieldName, String value) {
        if ("uuid_training".equals(fieldName)) {
            return existsById(value);
        } else if ("uuid_monitoring".equals(fieldName)) {
            return existsByUuidMonitoring(value);
        }
        return false;
    }
}