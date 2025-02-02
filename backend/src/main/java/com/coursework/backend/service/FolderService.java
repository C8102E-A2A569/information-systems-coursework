package com.coursework.backend.service;

import com.coursework.backend.dto.FolderDto;
import com.coursework.backend.entity.Folder;
import com.coursework.backend.repository.FolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FolderService {
    @Autowired
    private FolderRepository folderRepository;

    public List<FolderDto> getFoldersByUser(String userLogin) {
        List<Folder> folders = folderRepository.findByUserLogin(userLogin);
        return folders.stream().map(Folder::toDto).collect(Collectors.toList());
    }
}
