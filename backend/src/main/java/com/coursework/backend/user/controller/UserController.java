package com.coursework.backend.user.controller;

import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user-login/{login}")
    public User getUserLogin(@PathVariable String login) {
        return userService.getByLogin(login);
    }
}
