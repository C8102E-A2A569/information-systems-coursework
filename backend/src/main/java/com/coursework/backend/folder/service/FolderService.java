package com.coursework.backend.folder.service;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.model.Folder;
import com.coursework.backend.folder.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {

    private final FolderRepository folderRepository;

    public List<FolderDto> getFoldersByUser(String userLogin) {
        List<Folder> folders = folderRepository.findByUserLogin(userLogin);
        return folders.stream().map(Folder::toDto).collect(Collectors.toList());
    }
}
