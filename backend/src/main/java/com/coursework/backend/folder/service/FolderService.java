package com.coursework.backend.folder.service;

import com.coursework.backend.exception.exceptions.FolderNotFoundException;
import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.dto.FolderDtoRequest;
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

    public List<FolderDto> getRootFoldersByUser() {
        final User user = userService.getCurrentUser();
        List<Folder> folders = folderRepository.findAllByUserAndParentFolder(user, null);
        return folders.stream().map(Folder::toDto).collect(Collectors.toList());
    }

    public List<FolderDto> getSubfolders(FolderDtoRequest request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("Id папки не указан");
        }

        final User user = userService.getCurrentUser();

        Folder parentFolder = folderRepository.findByIdAndUser(request.getId(), user)
                .orElseThrow(() -> new FolderNotFoundException("Папка с указанным ID не найдена"));

        List<Folder> subFolders = folderRepository.findAllByUserAndParentFolder(user, parentFolder);
        return subFolders.stream().map(Folder::toDto).collect(Collectors.toList());
    }
}
