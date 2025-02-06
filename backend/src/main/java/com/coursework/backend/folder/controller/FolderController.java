package com.coursework.backend.folder.controller;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.dto.FolderDtoRequest;
import com.coursework.backend.folder.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    //получить список всех корневых папок пользователя автоматически при входе на сайт
    @GetMapping("/root")
    public List<FolderDto> getRootFoldersByUser() {
        return folderService.getRootFoldersByUser();
    }

    @PostMapping("/subfolders")
    public List<FolderDto> geSubfolders(@RequestBody FolderDtoRequest request) {
        return folderService.getSubfolders(request);
    }
}
