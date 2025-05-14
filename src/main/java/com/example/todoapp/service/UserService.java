package com.example.todoapp.service;

import com.example.todoapp.domain.User;
import com.example.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String name, String email, String rawPassword) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("そのメールアドレスは既に登録されています。");
        }
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User newUser = new User();
        newUser.setName(name);
        newUser.setEmail(email);
        newUser.setPassword(hashedPassword);
        return userRepository.save(newUser);
    }

    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }
}