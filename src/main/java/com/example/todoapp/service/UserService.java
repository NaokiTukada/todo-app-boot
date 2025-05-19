package com.example.todoapp.service;

//ユーザー登録やログインといった、アプリケーションの主要な処理。

import com.example.todoapp.domain.User;
import com.example.todoapp.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public User registerUser(String email, String password, String confirmPassword) {
        if (!isValidEmailFormat(email)) {
            throw new IllegalArgumentException("XXXX@XXX.XXXの形式で入力してください");
        }
        if (password == null || password.length() < 8) {
            throw new IllegalArgumentException("パスワードは８文字以上で設定してください");
        }
        if (!password.equals(confirmPassword)) {
            throw new IllegalArgumentException("パスワードと確認用が異なってる可能性があります");
        }
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPassword(passwordEncoder.encode(password));
        return userRepository.save(newUser);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Transactional
    public void updateLastLogin(User user) {
        user.setLastLogin(LocalDateTime.now());
        userRepository.save(user);
    }

    // メールアドレスの形式をチェックするメソッド
    private boolean isValidEmailFormat(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}