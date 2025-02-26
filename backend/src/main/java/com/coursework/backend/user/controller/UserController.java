package com.coursework.backend.user.controller;

import com.coursework.backend.user.dto.AuthResponseDto;
import com.coursework.backend.user.dto.PatchUserDto;
import com.coursework.backend.user.dto.UserDto;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user-login/{login}")
    public User getUserLogin(@PathVariable String login) {
        return userService.getByLogin(login);
    }

    @PatchMapping("/profile")
    public AuthResponseDto patchUser(@Valid @RequestBody PatchUserDto patchUserDto) {
        return userService.patchUser(patchUserDto);
    }

}
