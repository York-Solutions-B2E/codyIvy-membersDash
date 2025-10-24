package com.example.benefits.controller;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import com.example.benefits.dto.GoogleAuthRequest;
import com.example.benefits.service.GoogleAuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {

    private final GoogleAuthService googleAuthService;

    public GoogleAuthController(GoogleAuthService googleAuthService) {
        this.googleAuthService = googleAuthService;

    }

    @PostMapping
    public ResponseEntity<?> exchangeCode(@RequestBody GoogleAuthRequest body) {
        String code = body.getCode();
        try {

            Map<String, Object> result = googleAuthService.authenticateWithGoogle(code);

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to exchange code for tokens", "details", e.getMessage()));
        }
    }
}