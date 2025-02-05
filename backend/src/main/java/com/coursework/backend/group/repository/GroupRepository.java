package com.coursework.backend.group.repository;

import com.coursework.backend.group.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Long> {
}

