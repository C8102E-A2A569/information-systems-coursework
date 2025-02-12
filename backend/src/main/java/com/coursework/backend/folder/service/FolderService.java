package com.coursework.backend.folder.service;

import com.coursework.backend.exception.exceptions.FolderNotFoundException;
import com.coursework.backend.exception.exceptions.FolderUpdateException;
import com.coursework.backend.folder.dto.CreateFolderRequest;
import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.dto.FolderDtoRequest;
import com.coursework.backend.folder.dto.PatchFolderDto;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public FolderDto createFolder(CreateFolderRequest request) {
        try {
            final User user = userService.getCurrentUser();
            if (request.getName() == null || request.getName().isEmpty()) {
                throw new IllegalArgumentException("Название папки не указано");
            }
            Folder parentFolder = request.getParentFolderId() != null
                    ? folderRepository.findByIdAndUser(request.getParentFolderId(), user)
                    .orElseThrow(() -> new RuntimeException("Родительская папка не найдена"))
                    : null;

            Folder newFolder = Folder.builder()
                    .parentFolder(parentFolder)
                    .user(user)
                    .name(request.getName())
                    .build();

            folderRepository.save(newFolder);
            return newFolder.toDto();

        } catch (Exception e) {
            throw new IllegalArgumentException("Не удалось создать папку");
        }
    }

    public FolderDto patchFolder(Long folderId, PatchFolderDto patchFolderDto) {
        if (patchFolderDto.getName() == null || patchFolderDto.getName().isEmpty()) {
            throw new FolderUpdateException("Новое название папки не указано");
        }

        User user = userService.getCurrentUser();
        Folder folder = folderRepository.findByIdAndUser(folderId, user)
                .orElseThrow(() -> new FolderNotFoundException("Папка с указанным ID не найдена"));

        folder.setName(patchFolderDto.getName());
        return folderRepository.save(folder).toDto();
    }

}
