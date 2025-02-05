package com.coursework.backend.user.service;

import com.coursework.backend.exception.exceptions.UserAlreadyExistsException;
import com.coursework.backend.security.JwtService;
import com.coursework.backend.user.dto.AuthResponseDto;
import com.coursework.backend.user.dto.LoginUserDto;
import com.coursework.backend.user.dto.RegisterUserDto;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(@Valid RegisterUserDto registerUserDto) {
        if (userRepository.existsByLogin(registerUserDto.getLogin())) {
            throw new UserAlreadyExistsException(
                    String.format("Пользователь с логином %s уже существует", registerUserDto.getLogin())
            );
        }

        User user = new User();
        user.setLogin(registerUserDto.getLogin());
        user.setName(registerUserDto.getName());
        user.setPassword(passwordEncoder.encode(registerUserDto.getPassword()));

        User savedUser = userService.save(user);
        String token = jwtService.generateToken(savedUser);

        return new AuthResponseDto(savedUser.getLogin(), savedUser.getName(), token);
    }

    public AuthResponseDto login(@Valid LoginUserDto loginUserDto) {
        try {
            final var user = userService.getByLogin(loginUserDto.getLogin());
            if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
                throw new IllegalArgumentException("Неверный логин или пароль");
            }
            String token = jwtService.generateToken(user);

            return new AuthResponseDto(user.getLogin(), user.getName(), token);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Неверный логин или пароль");
        }
    }
}

