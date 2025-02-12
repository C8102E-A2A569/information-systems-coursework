package com.coursework.backend.folder.controller;

import com.coursework.backend.folder.dto.CreateFolderRequest;
import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.dto.FolderDtoRequest;
import com.coursework.backend.folder.dto.PatchFolderDto;
import com.coursework.backend.folder.model.Folder;
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

    @PostMapping("/subFolders")
    public List<FolderDto> geSubFolders(@RequestBody FolderDtoRequest request) {
        return folderService.getSubFolders(request);
    }

    @PostMapping
    public FolderDto createFolder(@RequestBody CreateFolderRequest request) {
        return folderService.createFolder(request);
    }

    @PatchMapping("/{id}")
    public FolderDto patchFolder(@PathVariable Long id, @RequestBody PatchFolderDto patchFolderDto) {
        return folderService.patchFolder(id, patchFolderDto);
    }
}
