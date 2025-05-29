package com.example.todoapp.controller;

//ユーザー登録やログインのリクエストをWebから受け付け、それぞれの処理を呼び出す窓口。

import com.example.todoapp.domain.User;
import com.example.todoapp.service.UserService;
import com.example.todoapp.util.JwtUtil;
import com.example.todoapp.service.UserDetailsServiceImpl;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

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


        try {
            User registeredUser = userService.registerUser(
                registrationRequest.getEmail(),
                registrationRequest.getPassword(),
                registrationRequest.getConfirmPassword()
                );
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED); // 登録成功
        } catch (IllegalArgumentException e) {
            // () を削除
            return new ResponseEntity<>("新規登録処理中にエラーが発生しました", HttpStatus.BAD_REQUEST); // バリデーションエラーなど
        } catch (RuntimeException e) {
            // () を削除
            return new ResponseEntity<>("新規登録処理中にエラーが発生しました", HttpStatus.CONFLICT); // メールアドレス重複など
        } catch (Exception e) {
            // その他の予期せぬエラー
            return new ResponseEntity<>("新規登録処理中にエラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest,
            jakarta.servlet.http.HttpServletResponse response) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String jwt = jwtUtil.generateToken(userDetails);

            // 追加:最終ログイン日時を更新
            userService.findByEmail(email).ifPresent(userService::updateLastLogin);
            
             // JWTをHttpOnlyクッキーにセット
            // application.propertiesのjwt.expiration（ミリ秒）を使う
            int maxAge = (int) (jwtUtil.getExpiration() / 1000); // 秒に変換
            Cookie cookie = new Cookie("jwt", jwt);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            cookie.setMaxAge(maxAge); // application.propertiesの値を反映
            cookie.setSecure(false); // 本番はtrue（httpsのみ）
            cookie.setAttribute("SameSite", "Strict");
            response.addCookie(cookie);

            return ResponseEntity.ok(Map.of("token", jwt)); // 必要ならbodyからtokenを除外してもOK

        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<>("メールアドレス、パスワードが誤っている可能性があります", HttpStatus.UNAUTHORIZED); 
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("メールアドレス、パスワードが誤っている可能性があります", HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            // その他の予期せぬエラー
            return new ResponseEntity<>("ログイン処理中にエラーが発生しました", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletResponse response) {
        ResponseCookie deleteCookie = ResponseCookie.from("jwt", "")
            .httpOnly(true)
            .secure(false) // 本番は true
            .path("/")
            .maxAge(0)
            .sameSite("Strict")
            .build();

        response.addHeader("Set-Cookie", deleteCookie.toString());
        return ResponseEntity.ok().build();
    }

    // リクエストボディを受け取るためのクラス
    @Data
    public static class RegistrationRequest {
        private String email;
        private String password;
        private String confirmPassword;
    }
}