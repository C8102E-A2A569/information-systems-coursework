package com.coursework.backend.controller;

import com.coursework.backend.dto.FolderDto;
import com.coursework.backend.entity.Folder;
import com.coursework.backend.service.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/folders")
public class FolderController {
    private FolderService folderService;

    //получить список всех папок пользователя автоматически при входе на сайт
    @GetMapping("/user/{userLogin}")
    public List<FolderDto> getFoldersByUser(@PathVariable String userLogin) {
        return folderService.getFoldersByUser(userLogin);
    }
}
