package com.example.benefits.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> {}) // enable CORS from WebConfig
            .csrf(csrf -> csrf.disable()) // disable CSRF for simplicity in dev
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/me").authenticated() // allow auth endpoints
                .anyRequest().permitAll() // allow everything for now
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(Customizer.withDefaults()) // enable JWT authentication
            );
        return http.build();
    }
}
