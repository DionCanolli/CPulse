package com.dioncanolli.cpulse_back_end.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

public class JWTTokenGenerator {

    private static final String JWT_KEY = "jxgEQeXHuPq8VdbyYENkANdudQ53YUn4";
    private static final String JWT_HEADER = "Authorization";

    public static String generateJWTToken(Authentication authentication, HttpServletResponse response){
        String jwtToken = Jwts.builder()
                .issuer("DTPulse e-commerce")
                .subject("JWT Token")
                .claim("username", authentication.getName())
                .claim("authorities", authentication.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .issuedAt(new Date())
                .expiration(new Date(new Date().getTime() + 86400000)) // 86400000 milliseconds -> 24h
                .signWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8)))
                .compact();
//        response.setHeader(JWT_HEADER, jwtToken);
        return jwtToken;
    }

    public static String extractUsernameFromToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(JWT_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.get("username", String.class);
    }
}