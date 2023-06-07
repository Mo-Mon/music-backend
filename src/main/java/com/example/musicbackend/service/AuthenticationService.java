package com.example.musicbackend.service;

import com.example.musicbackend.payload.request.AuthenticationRequest;
import com.example.musicbackend.payload.request.RegisterUserRequest;
import com.example.musicbackend.payload.response.AuthenticationResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterUserRequest request);

    void logoutUserAllDevice(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException;

    AuthenticationResponse authenticate(AuthenticationRequest request);

    AuthenticationResponse refreshToken(HttpServletRequest request,
                                        HttpServletResponse response) throws IOException;
}
