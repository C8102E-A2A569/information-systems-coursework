package com.coursework.backend.folder.repository;

import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    List<Folder> findAllByUser(User user);

    List<Folder> findAllByUserAndParentFolder(User user, Folder folder);
    Optional<Folder> findByIdAndUser(Long id, User user);

}
