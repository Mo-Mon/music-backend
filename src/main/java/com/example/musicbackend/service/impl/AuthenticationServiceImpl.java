package com.example.musicbackend.service.impl;

import com.example.musicbackend.Utils.DBLogicUtil;
import com.example.musicbackend.config.security.JwtProvider;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.exception.custom.AuthException;
import com.example.musicbackend.exception.custom.BadRequestException;
import com.example.musicbackend.payload.request.AuthenticationRequest;
import com.example.musicbackend.payload.request.RegisterUserRequest;
import com.example.musicbackend.payload.response.AuthenticationResponse;
import com.example.musicbackend.repository.RoleRepository;
import com.example.musicbackend.repository.UserRepository;
import com.example.musicbackend.service.AuthenticationService;
import com.example.musicbackend.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtProvider jwtProvider;

    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    private final TokenService tokenService;


    @Override
    public AuthenticationResponse register(RegisterUserRequest request){
        User oldUser = userRepository.findByEmail(request.getEmail()).orElse(null);
        if(oldUser != null){
            throw new BadRequestException("đã tồn tại tài khoản sử dụng email này");
        }
        var user = User
                .builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(request.getRoles().stream().map(name -> roleRepository.findByName(name).orElseThrow(() -> new BadRequestException("role nhập vào không tồn tại: "+ name))).collect(Collectors.toSet()))
                .build();
        DBLogicUtil.setupCreate(user,user);
        userRepository.save(user);
        return getAuthenticationResponse(user);
    }

    @Override
    public void logoutUserAllDevice(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String refreshToken;
        final String email;
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "refresh token not exist");
            throw new AuthException("refresh token not found");
        }
        refreshToken = authHeader.substring(7);
        email = jwtProvider.extractEmail(refreshToken);
        userRepository.findByEmail(email).ifPresent(tokenService::revokeAllTokenByUser);
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
        return getAuthenticationResponse(user);
    }

    @Override
    public AuthenticationResponse refreshToken(HttpServletRequest request,
                                               HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String email;
        if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "refresh token not exist");
            throw new AuthException("refresh token not found");
        }
        refreshToken = authHeader.substring(7);

        email = jwtProvider.extractEmail(refreshToken);

        if(StringUtils.hasText(email)){
            var user = this.userRepository.findByEmail(email)
                    .orElseThrow();
            if(jwtProvider.isTokenValid(refreshToken) && tokenService.checkExpiredAndRevokeRefreshToken(refreshToken)){
                var accessToken = jwtProvider.generateAccessToken(new HashMap<>(),user);
                tokenService.revokeAllAccessTokenByUser(user);
                tokenService.saveTokenForUser(accessToken, user, false);
                return AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
            }else{
                // Nếu access token đã hết hạn, trả về HTTP status code 401 Unauthorized
                throw new AuthException("Access token has expired");
            }
        }
        throw new AuthException("refresh token not found");

    }

    private AuthenticationResponse getAuthenticationResponse(User user) {
        var jwtToken = jwtProvider.generateAccessToken(new HashMap<>(), user);
        var jwtRefreshToken = jwtProvider.generateRefreshToken(new HashMap<>(), user);
        tokenService.revokeAllAccessTokenByUser(user);
        tokenService.saveTokenForUser(jwtToken,user, false);
        tokenService.saveTokenForUser(jwtRefreshToken,user, true);
        return AuthenticationResponse
                .builder()
                .accessToken(jwtToken)
                .refreshToken(jwtRefreshToken)
                .build();
    }
}
