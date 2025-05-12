package com.example.todoapp.controller;

import com.example.todoapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            // ここでパスワードの照合とJWTの生成を行います（次のステップで実装）
            // Spring Securityの AuthenticationManager を利用するのが一般的です
            return ResponseEntity.ok(Map.of("token", "DUMMY_TOKEN")); // 仮のトークン
        } else {
            return new ResponseEntity<>("メールアドレスまたはパスワードが間違っています。", HttpStatus.UNAUTHORIZED);
        }
    }
}