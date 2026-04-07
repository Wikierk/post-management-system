package com.jowk.auth.controller;

import com.jowk.auth.dto.GoogleLoginRequest;
import com.jowk.auth.dto.LoginRequest;
import com.jowk.auth.dto.RegisterRequest;
import com.jowk.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest request) {
        authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequest request, HttpServletResponse response) {
        try {
            authService.loginUser(request, response);
            return ResponseEntity.ok().body("{\"message\": \"Login successful! Token saved in cookies.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Login failed: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/oauth2/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody @Valid GoogleLoginRequest request, HttpServletResponse response) {
        try {
            authService.loginWithGoogle(request, response);
            return ResponseEntity.ok().body("{\"message\": \"Google login successful! Token saved in cookies.\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"error\": \"Google Login failed: " + e.getMessage() + "\"}");
        }
    }
}

