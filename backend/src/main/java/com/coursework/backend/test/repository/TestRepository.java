package com.coursework.backend.test.repository;

import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.group.model.Group;
import com.coursework.backend.test.model.AccessToTests;
import com.coursework.backend.test.model.Test;
import com.coursework.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TestRepository extends JpaRepository<Test, String> {
    boolean existsById(String id);
    Test findById(Test test);
    List<Test> findByIdOrNameOrUuidMonitoring(String id, String name, String uuidMonitoring);
    boolean existsByUuidMonitoring(String uuidMonitoring);
    Optional<Test> findByIdOrName(String id, String name);
    List<Test> findAllByGroup(Group group);

    default boolean existsByField(String fieldName, String value) {
        if ("uuid_training".equals(fieldName)) {
            return existsById(value);
        } else if ("uuid_monitoring".equals(fieldName)) {
            return existsByUuidMonitoring(value);
        }
        return false;
    }
}