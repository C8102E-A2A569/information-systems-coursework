package com.coursework.backend.user.service;

import com.coursework.backend.exception.exceptions.UserAlreadyExistsException;
import com.coursework.backend.exception.exceptions.UserNotFoundException;
import com.coursework.backend.security.JwtService;
import com.coursework.backend.user.dto.PatchUserDto;
import com.coursework.backend.user.dto.UserDto;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public User save(User user) {
        return repository.save(user);
    }

    public User getByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
    }

    @Transactional
    public UserDto updateUser(String currentLogin, PatchUserDto patchUserDto) {
        User user = repository.findByLogin(currentLogin)
                .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));

        if (patchUserDto.getLogin() != null && !patchUserDto.getLogin().isBlank()) {
            if (!patchUserDto.getLogin().equals(user.getLogin()) &&
                    repository.existsByLogin(patchUserDto.getLogin())) {
                throw new UserAlreadyExistsException("Логин уже используется");
            }
            user.setLogin(patchUserDto.getLogin());
        }

        if (patchUserDto.getName() != null && !patchUserDto.getName().isBlank()) {
            user.setName(patchUserDto.getName());
        }

        if (patchUserDto.getPassword() != null && !patchUserDto.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(patchUserDto.getPassword()));
        }

        return UserDto.fromEntity(repository.save(user));
    }

    public UserDetailsService userDetailsService() {
        return this::getByLogin;
    }

    public User getCurrentUser() {
        var username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return getByLogin(username);
    }
}
