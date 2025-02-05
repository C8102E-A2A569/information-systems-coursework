package com.coursework.backend.user.service;

import com.coursework.backend.exception.exceptions.UserNotFoundException;
import com.coursework.backend.security.JwtService;
import com.coursework.backend.user.model.User;
import com.coursework.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repository;

    private final JwtService jwtService;

    public User save(User user) {
        return repository.save(user);
    }

//    public User create(User user) {
//        if (repository.existsByLogin(user.getLogin())) {
//            throw new RuntimeException("Пользователь с таким именем уже существует");
//        }
//        return save(user);
//    }

    public User getByLogin(String login) {
        return repository.findByLogin(login)
                .orElseThrow(() -> new UserNotFoundException("Пользователь с логином %s не найден"));
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
