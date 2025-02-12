package com.coursework.backend.folder.service;

import com.coursework.backend.exception.exceptions.FolderCreationException;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserService userService;

    public List<FolderDto> getRootFoldersByUser() {
        User user = userService.getCurrentUser();
        return folderRepository.findAllByUserAndParentFolder(user, null)
                .stream()
                .map(Folder::toDto)
                .collect(Collectors.toList());
    }

    public List<FolderDto> getSubFolders(FolderDtoRequest request) {
        if (request == null || request.getId() == null) {
            throw new IllegalArgumentException("Id папки не указан");
        }

        User user = userService.getCurrentUser();
        Folder parentFolder = folderRepository.findByIdAndUser(request.getId(), user)
                .orElseThrow(() -> new FolderNotFoundException("Папка с указанным ID не найдена"));

        return folderRepository.findAllByUserAndParentFolder(user, parentFolder)
                .stream()
                .map(Folder::toDto)
                .collect(Collectors.toList());
    }

    public FolderDto createFolder(CreateFolderRequest request) {
        if (request.getName() == null || request.getName().isEmpty()) {
            throw new FolderCreationException("Название папки не указано");
        }

        User user = userService.getCurrentUser();
        Folder parentFolder = getParentFolderIfExists(request.getParentFolderId(), user);

        Folder newFolder = Folder.builder()
                .parentFolder(parentFolder)
                .user(user)
                .name(request.getName())
                .build();

        folderRepository.save(newFolder);
        return newFolder.toDto();
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

    private Folder getParentFolderIfExists(Long parentFolderId, User user) {
        if (parentFolderId == null) {
            return null;
        }
        return folderRepository.findByIdAndUser(parentFolderId, user)
                .orElseThrow(() -> new FolderNotFoundException("Родительская папка не найдена"));
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
