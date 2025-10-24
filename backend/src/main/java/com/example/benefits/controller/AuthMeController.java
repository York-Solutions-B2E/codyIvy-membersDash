package com.example.benefits.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.benefits.repository.UserRepository;
import com.example.benefits.dto.AuthMeDTO;
import com.example.benefits.model.User;
import com.example.benefits.model.UserNotFoundException;
import com.example.benefits.model.Member;
import com.example.benefits.service.MemberLookupService;

@RestController
@RequestMapping("/api/auth")
public class AuthMeController {
    private final UserRepository userRepository;
    private final MemberLookupService memberLookupService;

    public AuthMeController(UserRepository userRepository, MemberLookupService memberLookupService) {
        this.userRepository = userRepository;
        this.memberLookupService = memberLookupService;
    }

    @GetMapping("/me")
    public ResponseEntity<AuthMeDTO> getMe(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String sub = jwt.getSubject();
        String provider = "google";

        User user = userRepository.findByAuthProviderAndAuthSub(provider, sub)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        Member member = memberLookupService.findMemberByUser(user);

        AuthMeDTO dto = new AuthMeDTO();
        dto.setUserId(user.getId());
        dto.setEmail(member.getEmail());
        dto.setName(user.getName());
        return ResponseEntity.ok(dto);
    }
}
