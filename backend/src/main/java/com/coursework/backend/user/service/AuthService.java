package com.coursework.backend.user.service;

import com.coursework.backend.exception.exceptions.UserAlreadyExistsException;
import com.coursework.backend.exception.exceptions.UserNotFoundException;
import com.coursework.backend.group.model.Role;
import com.coursework.backend.security.JwtService;
import com.coursework.backend.user.dto.AuthResponseDto;
import com.coursework.backend.user.dto.LoginUserDto;
import com.coursework.backend.user.dto.RegisterUserDto;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDto register(RegisterUserDto registerUserDto) {
        if (userRepository.existsByLogin(registerUserDto.getName()))
            throw new UserAlreadyExistsException(
                    String.format("Name %s already exists", registerUserDto.getName())
            );

        User user = User
                .builder()
                .name(registerUserDto.getName())
                .password(passwordEncoder.encode(registerUserDto.getPassword()))
                .build();

        user = userRepository.save(user);

        String token = jwtService.generateToken(user);
        return new AuthResponseDto(
                user.getLogin(),
                user.getName(),
                token
        );
    }

    public AuthResponseDto login(LoginUserDto loginUserDto) {
        User user = userRepository.findByLogin(loginUserDto.getLogin())
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("Username %s not found", loginUserDto.getName())
                ));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginUserDto.getName(), loginUserDto.getPassword())
        );

        String token = jwtService.generateToken(user);
        return new AuthResponseDto(
                user.getLogin(),
                user.getName(),
                token
        );
    }
}
