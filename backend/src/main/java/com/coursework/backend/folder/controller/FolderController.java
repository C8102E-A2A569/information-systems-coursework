package com.coursework.backend.folder.controller;

import com.coursework.backend.folder.dto.FolderDto;
import com.coursework.backend.folder.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/folders")
@RequiredArgsConstructor
public class FolderController {

    private final FolderService folderService;

    //получить список всех папок пользователя автоматически при входе на сайт
    @GetMapping("/user/{userLogin}")
    public List<FolderDto> getFoldersByUser(@PathVariable String userLogin) {
        return folderService.getFoldersByUser(userLogin);
    }
}
