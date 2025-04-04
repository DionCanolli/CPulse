//package com.dioncanolli.dtpulse_back_end.security;
//
//import com.dioncanolli.dtpulse_back_end.entity.JWTToken;
//import com.dioncanolli.dtpulse_back_end.service.MyService;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class JWTValidatorFilter extends OncePerRequestFilter {
//
//    private final MyService myService;
//
//    public JWTValidatorFilter(MyService myService) {
//        this.myService = myService;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String authorizationHeader = request.getHeader("Authorization");
//        JWTToken token = new JWTToken(authorizationHeader);
//
//        if (authorizationHeader != null && !authorizationHeader.isEmpty() && !myService.isTokenBlacklisted(token.getJwtValue())) {
//
//            Claims claims = Jwts.parser()
//                    .verifyWith(Keys.hmacShaKeyFor("jxgEQeXHuPq8VdbyYENkANdudQ53YUn4".getBytes(StandardCharsets.UTF_8)))
//                    .build()
//                    .parseSignedClaims(authorizationHeader)
//                    .getPayload();
//
//            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//            String authority = String.valueOf(claims.get("authorities"));
//            authority = authority.substring(1, authority.length() - 1);
//            authorities.add(new SimpleGrantedAuthority(authority));
//
//            SecurityContextHolder
//                    .getContext()
//                    .setAuthentication(
//                            new UsernamePasswordAuthenticationToken(
//                                    String.valueOf(claims.get("username")),
//                                    null,
//                                    authorities)
//                    );
//            }
//
//        filterChain.doFilter(request, response);
//    }
//
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getServletPath().equals("/users/login") || request.getServletPath().equals("/users/signup");
//    }
//}

package com.dioncanolli.cpulse_back_end.filter;

import com.dioncanolli.cpulse_back_end.entity.JWTTokenBlacklist;
import com.dioncanolli.cpulse_back_end.security.CustomUserDetailsService;
import com.dioncanolli.cpulse_back_end.service.MyService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

public class JWTValidatorFilter extends OncePerRequestFilter {

    private final MyService myService;
    private final CustomUserDetailsService customUserDetailsService;

    public JWTValidatorFilter(MyService myService, CustomUserDetailsService customUserDetailsService) {
        this.myService = myService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        JWTTokenBlacklist token = new JWTTokenBlacklist(authorizationHeader);

        if (authorizationHeader != null && !authorizationHeader.isEmpty()
                && !myService.isTokenBlacklisted(token.getJwtValue())){

            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor("jxgEQeXHuPq8VdbyYENkANdudQ53YUn4".getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(authorizationHeader)
                    .getPayload();

            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            String authority = String.valueOf(claims.get("authorities"));
            authority = authority.substring(1, authority.length() - 1);
            authorities.add(new SimpleGrantedAuthority(authority));

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    String.valueOf(claims.get("username")), null, authorities);

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        filterChain.doFilter(request, response);
    }

//    Nese nuk e bon permit urlt per signup dhe login ne security configuration me requestMatchers, boje qe validatori mos mi validu kto
//    2 requesta:
//    @Override
//    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
//        return request.getServletPath().equals("/users/login") || request.getServletPath().equals("/users/signup");
//    }
}

