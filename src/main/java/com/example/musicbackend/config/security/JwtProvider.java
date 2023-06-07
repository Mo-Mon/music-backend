package com.example.musicbackend.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private final String secretKey = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    private final Long accessTokenExpiration = 300000L;

    private final Long refreshTokenExpiration = 86400000L;


    public String extractEmail(String jwt){
        return extractClaims(jwt, Claims::getSubject);
    }

    public String generateRefreshToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ){
        return buildToken(extractClaims, userDetails, refreshTokenExpiration);
    }

    public String generateAccessToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails
    ){
        return buildToken(extractClaims, userDetails, accessTokenExpiration);
    }

    private String buildToken(
            Map<String, Object> extractClaims,
            UserDetails userDetails,
            Long jwtExpiration
    ){
        return Jwts
                .builder()
                .setClaims(extractClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Boolean isTokenValid(String jwtToken, UserDetails userDetails){
        String email = extractEmail(jwtToken);
        return email.equals(userDetails.getUsername()) && !isTokenExpried(jwtToken);
    }

    private boolean isTokenExpried(String jwtToken) {
        return extractClaims(jwtToken, Claims::getExpiration).before(new Date());
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public <T> T extractClaims(String jwt, Function<Claims,T> claimsTFunction){
        Claims claims = extractAllClaims(jwt);
        return claimsTFunction.apply(claims);
    }


}
