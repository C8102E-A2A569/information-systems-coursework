package com.coursework.backend.user.controller;

import com.coursework.backend.user.dto.AuthResponseDto;
import com.coursework.backend.user.dto.LoginUserDto;
import com.coursework.backend.user.dto.RegisterUserDto;
import com.coursework.backend.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final AuthService authService;

    // регистрация
    @GetMapping("/sign-up")
    public AuthResponseDto signUp(RegisterUserDto user) {
        return authService.register(user);
    }

    // вход
    @GetMapping("/sign-in")
    public AuthResponseDto signIn(LoginUserDto user) {
        return authService.login(user);
    }
}
