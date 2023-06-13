package com.example.musicbackend.service.impl;

import com.example.musicbackend.entity.Token;
import com.example.musicbackend.entity.User;
import com.example.musicbackend.repository.TokenRepository;
import com.example.musicbackend.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepository tokenRepository;

    @Override
    public void saveTokenForUser(String jwtToken, User user, Boolean isRefresh){
        Token token = Token.builder()
                .token(jwtToken)
                .revoked(false)
                .expired(false)
                .isRefreshToken(isRefresh)
                .user(user)
                .build();
        tokenRepository.save(token);
    }
    @Override
    public void revokeAllAccessTokenByUser(User user){
        var tokens = tokenRepository.findAllValidAccessTokenByUser(user.getId());
        if(tokens == null || tokens.isEmpty()){
            return;
        }
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(tokens);
    }

    @Override
    public void revokeAllTokenByUser(User user){
        var tokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if(tokens == null || tokens.isEmpty()){
            return;
        }
        tokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(tokens);
    }

    @Override
    public  Boolean checkExpiredAndRevokeRefreshToken(String jwtToken){

        return tokenRepository
                .findByToken(jwtToken)
                .map(token -> !token.expired && !token.revoked && token.isRefreshToken)
                .orElse(false);
    }

    @Override
    public Boolean checkExpiredAndRevokeAccessToken(String jwtToken){
        return tokenRepository
                .findByToken(jwtToken)
                .map(token -> !token.expired && !token.revoked && !token.isRefreshToken)
                .orElse(false);
    }

    @Override
    public Token findByToken(String token){
        return tokenRepository.findByToken(token).orElse(null);
    }

    @Override
    public void closeToken(Token token){
        token.setRevoked(true);
        token.setExpired(true);
        tokenRepository.save(token);
    }

}
