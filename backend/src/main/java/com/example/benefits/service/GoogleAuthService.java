package com.example.benefits.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import com.example.benefits.repository.MemberRepository;
import com.example.benefits.repository.UserRepository;
import com.example.benefits.model.User;
import com.example.benefits.model.Member;
import com.example.benefits.model.GoogleAuthException;

@Service
public class GoogleAuthService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final DummyDataService dummyDataService;
    private final String googleClientId;
    private final String googleClientSecret;
    private final String googleRedirectUri;

    public GoogleAuthService(
            MemberRepository memberRepository,
            UserRepository userRepository,
            RestTemplate restTemplate,
            DummyDataService dummyDataService,
            @Value("${google.client.id}") String googleClientId,
            @Value("${google.client.secret}") String googleClientSecret,
            @Value("${google.redirect.uri}") String googleRedirectUri) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.dummyDataService = dummyDataService;
        this.googleClientId = googleClientId;
        this.googleClientSecret = googleClientSecret;
        this.googleRedirectUri = googleRedirectUri;
    }

    public Map<String, Object> authenticateWithGoogle(String code) {
        if (code == null) {
            throw new GoogleAuthException("Missing authorization code");
        }

        Map<String, Object> tokenData = exchangeCodeForTokens(code);

        String accessToken = (String) tokenData.get("access_token");
        if (accessToken == null) {
            throw new GoogleAuthException("No access token received from Google");
        }
        Map<String, Object> userInfo = fetchUserInfoFromGoogle(accessToken);

        User user = findOrCreateUser(userInfo);
        Member member = findOrCreateMember(user, userInfo);
        dummyDataService.ensureDummyDataForMember(member);

        return Map.of(
                "tokens", tokenData,
                "user_info", userInfo);
    }

    private Map<String, Object> exchangeCodeForTokens(String code) {
        // Build request parameters
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", googleClientId);
        params.add("client_secret", googleClientSecret);
        params.add("redirect_uri", googleRedirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        // Create http request entity
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        try {
            // make call
            ResponseEntity<?> googleResponse = restTemplate.postForEntity(
                    "https://oauth2.googleapis.com/token", requestEntity, Map.class);
            // Check if successful
            if (!googleResponse.getStatusCode().is2xxSuccessful()) {
                throw new GoogleAuthException("Invalid token response from Google");
            }
            // validate response and extract body
            Map<String, Object> tokenData = (Map<String, Object>) googleResponse.getBody();

            return tokenData;

        } catch (RestClientException e) {
            throw new GoogleAuthException("Failed to communicate with Google token endpoint", e);
        }
    }

    private Map<String, Object> fetchUserInfoFromGoogle(String accessToken) {
        HttpHeaders userInfoHeaders = new HttpHeaders();
        userInfoHeaders.setBearerAuth(accessToken); // ← Using the access token here
        HttpEntity<Void> userInfoRequest = new HttpEntity<>(userInfoHeaders);

        try {
            ResponseEntity<?> userInfoResponse = restTemplate.exchange(
                    "https://www.googleapis.com/oauth2/v3/userinfo",
                    org.springframework.http.HttpMethod.GET,
                    userInfoRequest,
                    Map.class);

            if (!userInfoResponse.getStatusCode().is2xxSuccessful()) {
                throw new GoogleAuthException("Failed to fetch user info from Google");
            }

            return (Map<String, Object>) userInfoResponse.getBody();

        } catch (RestClientException e) {
            throw new GoogleAuthException("Error fetching user info from Google", e);
        }
    }

    public User findOrCreateUser(Map<String, Object> userInfo) {
        String sub = (String) userInfo.get("sub");
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        return userRepository.findByAuthProviderAndAuthSub("google", sub)
                .orElseGet(() -> userRepository.save(User.builder()
                        .authProvider("google")
                        .authSub(sub)
                        .email(email)
                        .name(name)
                        .build()));
    }

    public Member findOrCreateMember(User user, Map<String, Object> userInfo) {
        String email = (String) userInfo.get("email");
        String firstName = (String) userInfo.get("given_name");
        String lastName = (String) userInfo.get("family_name");

        return memberRepository.findByUser(user).orElseGet(() -> {
            Member newMember = Member.builder()
                    .user(user)
                    .firstName(firstName)
                    .lastName(lastName)
                    .email(email)
                    .build();
            return memberRepository.save(newMember);
        });
    }
}
