package com.coursework.backend.folder.service;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserService userService;
    Logger logger = Logger.getLogger("Folder info");

    public List<FolderDto> getFoldersByUser() {
        final User user = userService.getCurrentUser();
        logger.log(Level.INFO, "User login: " + user.getLogin());
        List<Folder> folders = folderRepository.findAllByUser(user);
        logger.log(Level.INFO, "Folders count: " + folders.size());
        return folders.stream().map(Folder::toDto).collect(Collectors.toList());
    }
}
