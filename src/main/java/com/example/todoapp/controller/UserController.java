package com.example.todoapp.controller;

//ユーザー登録やログインのリクエストをWebから受け付け、それぞれの処理を呼び出す窓口。

import com.example.todoapp.domain.User;
import com.example.todoapp.service.UserService;
import com.example.todoapp.util.JwtUtil;
import com.example.todoapp.service.UserDetailsServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistrationRequest registrationRequest) {
        List<String> errors = new ArrayList<>();

        // パターン別、エラーメッセージ
        if (!isValidEmail(registrationRequest.getEmail())) {
            errors.add("XXXX@XXX.XXXの形式で入力してください");
        }
        if (registrationRequest.getPassword() == null || registrationRequest.getPassword().length() < 8) {
            errors.add("パスワードは８文字以上で設定してください");
        }
        if (!registrationRequest.getPassword().equals(registrationRequest.getConfirmPassword())) {
            errors.add("パスワードと確認用が異なってる可能性があります");
        }

        if (!errors.isEmpty()) {
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST); // エラーメッセージと適切なステータスコードを返す
        }

        try {
            User registeredUser = userService.registerUser(
                registrationRequest.getEmail(),
                registrationRequest.getPassword()
            );
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED); // 登録成功
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST); // バリデーションエラーなど
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT); // メールアドレス重複など
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
    String email = loginRequest.get("email");
    String password = loginRequest.get("password");

    try {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        String jwt = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("token", jwt)); // ログイン成功時に JWT を返す
    } catch (UsernameNotFoundException e) {
        return new ResponseEntity<>("メールアドレス、パスワードが誤っている可能性があります", HttpStatus.UNAUTHORIZED);    
    } catch (AuthenticationException e) {
        return new ResponseEntity<>("メールアドレス、パスワードが誤っている可能性があります", HttpStatus.UNAUTHORIZED);
    } catch (Exception e) {
        // その他の予期せぬエラー
        return new ResponseEntity<>("ログイン処理中にエラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

    // リクエストボディを受け取るためのクラス
    @Data
    public static class RegistrationRequest {
        private String email;
        private String password;
        private String confirmPassword;
    }

    // メールアドレスの形式をチェックするメソッド
    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
    }
}