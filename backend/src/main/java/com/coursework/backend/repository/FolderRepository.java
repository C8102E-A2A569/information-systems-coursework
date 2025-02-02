package com.coursework.backend.repository;

import com.coursework.backend.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByUserLogin(String userLogin);

}
