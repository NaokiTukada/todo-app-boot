package com.example.todoapp.controller;

import com.example.todoapp.domain.User;
import com.example.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;


    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String email = request.get("email");
        String password = request.get("password");
        try {
            userService.registerUser(name, email, password);
            return new ResponseEntity<>("登録が完了しました。", HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String password = request.get("password");
        Optional<User> userOptional = userService.findUserByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                
                return ResponseEntity.ok(Map.of("token", "DUMMY_TOKEN")); 
            } else {
                return new ResponseEntity<>("メールアドレスまたはパスワードが間違っています。", HttpStatus.UNAUTHORIZED);
            }
        } else {
            return new ResponseEntity<>("メールアドレスまたはパスワードが間違っています。", HttpStatus.UNAUTHORIZED);
        }
    }
}