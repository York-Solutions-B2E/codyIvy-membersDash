package com.example.benefits.controller;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.*;
import com.example.benefits.model.User;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.example.benefits.repository.UserRepository;

import java.util.Map;

@RestController
@RequestMapping("/api/auth/google")
public class GoogleAuthController {
    private final RestTemplate restTemplate = new RestTemplate();
    private final UserRepository userRepository;

    public GoogleAuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

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

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<?> googleResponse = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token", request, Map.class);

            if (!googleResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Failed to get tokens from Google");
            }

            Map<String, Object> tokenData = (Map<String, Object>) googleResponse.getBody();
            String accessToken = (String) tokenData.get("access_token");

            HttpHeaders userInfoHeaders = new HttpHeaders();
            userInfoHeaders.setBearerAuth(accessToken);
            HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

            ResponseEntity<?> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    HttpMethod.GET,
                    userInfoRequest,
                    Map.class);

            if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to get user info from Google");
            }

            Map<String, Object> userInfo = (Map<String, Object>) userInfoResponse.getBody();

            String sub = (String) userInfo.get("sub");
            String email = (String) userInfo.get("email");
            String name = (String) userInfo.get("name");

            userRepository.findByAuthProviderAndAuthSub("google", sub).orElseGet(() -> {
                User newUser = User.builder()
                .authProvider("google")
                .authSub(sub)
                .email(email)
                .name(name)
                .build();
                return userRepository.save(newUser);
            });
            

            return ResponseEntity.ok(Map.of(
                    "tokens", tokenData,
                    "user_info", userInfo));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to exchange code for tokens", "details", e.getMessage()));
        }
    }
}