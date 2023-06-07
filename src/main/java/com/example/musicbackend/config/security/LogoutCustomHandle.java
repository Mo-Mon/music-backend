package com.example.musicbackend.config.security;

import com.example.musicbackend.repository.TokenRepository;
import com.example.musicbackend.repository.UserRepository;
import com.example.musicbackend.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class LogoutCustomHandle implements LogoutHandler {

    private final TokenRepository tokenRepository;

    private final JwtProvider jwtProvider;

    private final UserRepository userRepository;

    private final TokenService tokenService;

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String jwtRefresh;
        if(!StringUtils.hasText(authHeader) || !authHeader.startsWith("Bearer ")){
            return;
        }
        jwtRefresh = authHeader.substring(7);
        var refreshToken = tokenRepository.findByToken(jwtRefresh)
                .orElse(null);
        if(refreshToken != null){
            tokenService.closeToken(refreshToken);
            var email = jwtProvider.extractEmail(jwtRefresh);
            userRepository.findByEmail(email).ifPresent(tokenService::revokeAllAccessTokenByUser);
            SecurityContextHolder.clearContext();
        }
    }

}
