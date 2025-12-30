package com.example.filemanager.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.filemanager.dto.request.LoginRequest;
import com.example.filemanager.dto.request.RegisterRequest;
import com.example.filemanager.dto.response.RegisterResponse;
import com.example.filemanager.model.User;
import com.example.filemanager.service.AuthService;
import com.example.filemanager.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {

        User user = userService.register(request);

        return ResponseEntity.ok(new RegisterResponse(user.getId(), user.getUsername(), user.getEmail()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        String token = authService.login(request.username(), request.password());

        return ResponseEntity.ok(Map.of("token", token));
    }
}
