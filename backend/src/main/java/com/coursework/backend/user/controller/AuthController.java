package com.coursework.backend.user.controller;

import com.coursework.backend.user.dto.AuthResponseDto;
import com.coursework.backend.user.dto.LoginUserDto;
import com.coursework.backend.user.dto.RegisterUserDto;
import com.coursework.backend.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    // регистрация
    @PostMapping("/sign-up")
    public AuthResponseDto signUp(@RequestBody RegisterUserDto user) {
        return authService.register(user);
    }

    // вход
    @PostMapping("/sign-in")
    public AuthResponseDto signIn(@Valid @RequestBody LoginUserDto user) {
        return authService.login(user);
    }
}
