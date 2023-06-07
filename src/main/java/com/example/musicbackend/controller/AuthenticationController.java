package com.example.musicbackend.controller;

import com.example.musicbackend.payload.request.AuthenticationRequest;
import com.example.musicbackend.payload.request.RegisterUserRequest;
import com.example.musicbackend.payload.response.AuthenticationResponse;
import com.example.musicbackend.service.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterUserRequest request
    ) {
        return ResponseEntity.ok(service.register(request));
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthenticationResponse> refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        return ResponseEntity.ok(service.refreshToken(request, response));
    }

    @GetMapping("/show")
    public ResponseEntity<?> show(
    ) {
        return ResponseEntity.ok("test link");
    }

    @PostMapping("/logout-all-device")
    public ResponseEntity<?> logoutAllDevice(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.logoutUserAllDevice(request, response);
        return ResponseEntity.ok("logout success");
    }

}
