package com.example.benefits.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {
    @Value("${google.client.id}")
    private String googleClientId;

    @Value("${google.client.secret}")
    private String googleClientSecret;

    @Value("${google.redirect.uri}")
    private String googleRedirectUri;

    @PostMapping
    public ResponseEntity<?> exchangeCode(@RequestBody Map<String, String> body) {
        String code = body.get("code");
        if (code == null) {
            return ResponseEntity.badRequest().body("Missing code");
        }

        // Prepare request to Google Token endpoint
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Send request
        RestTemplate rest = new RestTemplate();
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<?> googleResponse = rest.postForEntity(
            "https://oauth2.googleapis.com/token", request, Map.class
        );

        // Return the tokens from Google directly to the frontend
        return ResponseEntity.status(googleResponse.getStatusCode()).body(googleResponse.getBody());
    }
}