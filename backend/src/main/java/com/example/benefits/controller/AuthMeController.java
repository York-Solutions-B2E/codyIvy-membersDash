package com.example.benefits.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.benefits.repository.UserRepository;
import com.example.benefits.repository.MemberRepository;
import com.example.benefits.dto.AuthMeDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import com.example.benefits.model.User;
import com.example.benefits.model.Member;

@RestController
@RequestMapping("/api/auth")
public class AuthMeController {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public AuthMeController(UserRepository userRepository, MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/me")
    public ResponseEntity<AuthMeDTO> getMe(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        String sub = jwt.getSubject();
        String provider = "google";
        

        User user = userRepository.findByAuthProviderAndAuthSub(provider, sub)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Member member = memberRepository.findByUser(user).orElse(null);

        AuthMeDTO dto = new AuthMeDTO();
        dto.setUserId(user.getId());
        dto.setEmail(member.getEmail());
        if (member != null) {
            dto.setFirstName(member.getFirstName());
            dto.setLastName(member.getLastName());
        }
        return ResponseEntity.ok(dto);
    }
}
