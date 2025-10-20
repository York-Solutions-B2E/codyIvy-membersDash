package com.example.benefits.service;

import com.example.benefits.repository.MemberRepository;
import com.example.benefits.repository.UserRepository;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.benefits.model.User;
import com.example.benefits.model.Member;

@Service
public class GoogleAuthService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    public GoogleAuthService(MemberRepository memberRepository, UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
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
