package com.dioncanolli.cpulse_back_end.security;

import com.dioncanolli.cpulse_back_end.filter.CustomUsernamePasswordAuthenticationFilter;
import com.dioncanolli.cpulse_back_end.filter.JWTValidatorFilter;
import com.dioncanolli.cpulse_back_end.service.MyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private MyService myService;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Lazy
    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .authorizeHttpRequests((requests) ->
                                requests
                                        .requestMatchers("/admin/**")
                                            .hasRole("ADMIN")
                                        .requestMatchers("/permitted/**")
                                            .permitAll()
                                        .anyRequest()
                                            .authenticated()
                )
                .csrf(AbstractHttpConfigurer::disable)
                .userDetailsService(customUserDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(configurer -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();
                    corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:4200")); // Allow specific origin
                    corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE")); // Allow necessary methods
                    corsConfig.setAllowedHeaders(Arrays.asList("*")); // Allow all headers
                    corsConfig.setExposedHeaders(Arrays.asList("Authorization"));
                    configurer.configurationSource(request -> corsConfig);
                })
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterAfter(new CustomUsernamePasswordAuthenticationFilter(authenticationManager, myService),
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new JWTValidatorFilter(myService, customUserDetailsService), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationManagerBuilder a){
        return a.getObject();
    }
}
