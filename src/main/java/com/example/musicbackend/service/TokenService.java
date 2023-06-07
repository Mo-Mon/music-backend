package com.example.musicbackend.service;

import com.example.musicbackend.entity.Token;
import com.example.musicbackend.entity.User;

public interface TokenService {
    void saveTokenForUser(String jwtToken, User user, Boolean isRefresh);

    void revokeAllAccessTokenByUser(User user);

    void revokeAllTokenByUser(User user);

    Boolean checkExpiredAndRevokeRefreshToken(String jwtToken);

    Boolean checkExpiredAndRevokeAccessToken(String jwtToken);

    Token findByToken(String token);

    void closeToken(Token token);
}
