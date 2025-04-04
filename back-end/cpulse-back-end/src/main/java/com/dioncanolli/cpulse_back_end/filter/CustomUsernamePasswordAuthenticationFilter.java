package com.dioncanolli.cpulse_back_end.filter;

import com.dioncanolli.cpulse_back_end.entity.User;
import com.dioncanolli.cpulse_back_end.service.MyService;
import com.dioncanolli.cpulse_back_end.utility.JWTTokenGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final MyService myService;

    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager, MyService myService) {
        this.authenticationManager = authenticationManager;
        this.myService = myService;
        setFilterProcessesUrl("/permitted/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        try {
            Map<String, String> credentials = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            List<SimpleGrantedAuthority> authorityList = new ArrayList<>();

            String email = credentials.get("email");
            String password = credentials.get("password");

            User user = myService.findUserByEmail(email);

            if (user != null){
                authorityList.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
                Authentication authenticationToken = new UsernamePasswordAuthenticationToken(email, password, authorityList);
                return authenticationManager.authenticate(authenticationToken);
            }
        } catch (IOException ignored) {}
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        return null;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String jwtToken = JWTTokenGenerator.generateJWTToken(authResult, response);
        Map<String, String> responseBody = Map.of("token", jwtToken);

        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(responseBody));
        response.getWriter().flush();

//        Nese don me rujt tokenin ne header te responses e jo me kthy:
//        response.setHeader("Authorization", jwtToken);
    }
}
