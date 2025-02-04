package com.coursework.backend.folder.service;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserService userService;

    public List<FolderDto> getFoldersByUser() {
        final User user = userService.getCurrentUser();
        List<Folder> folders = folderRepository.findByUserLogin(user.getLogin());
        return folders.stream().map(Folder::toDto).collect(Collectors.toList());
    }
}
