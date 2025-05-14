package com.example.todoapp.controller;

import com.example.todoapp.domain.User;
import com.example.todoapp.service.UserService;
import com.example.todoapp.util.JwtUtil;
import com.example.todoapp.service.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<User> register(@RequestBody Map<String, String> registrationRequest) {
        String email = registrationRequest.get("email");
        String password = registrationRequest.get("password");
        User registeredUser = userService.registerUser(email, password);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
            String jwt = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(Map.of("token", jwt));
        } catch (Exception e) {
            return new ResponseEntity<>("認証に失敗しました", HttpStatus.UNAUTHORIZED);
        }
    }
}