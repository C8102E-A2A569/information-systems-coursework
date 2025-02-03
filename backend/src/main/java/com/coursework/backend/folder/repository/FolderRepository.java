package com.coursework.backend.folder.repository;

import com.coursework.backend.folder.model.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findByUserLogin(String userLogin);

}
