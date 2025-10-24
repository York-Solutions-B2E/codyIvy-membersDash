package com.example.benefits.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.benefits.repository.UserRepository;
import com.example.benefits.repository.MemberRepository;
import com.example.benefits.model.User;
import com.example.benefits.model.Member;
import com.example.benefits.model.MemberNotFoundException;
import com.example.benefits.model.UserNotFoundException;

@Service
@Transactional(readOnly = true)
public class MemberLookupService {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    public MemberLookupService(UserRepository userRepository, MemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
    }

    public Member findMemberByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email"));

        return memberRepository.findByUser(user)
            .orElseThrow(() -> new MemberNotFoundException("Member not found for user"));
    }

    public Member findMemberByAuthProviderAndSub(String provider, String sub) {
        User user = userRepository.findByAuthProviderAndAuthSub(provider, sub)
            .orElseThrow(() -> new UserNotFoundException("User not found for provider and sub"));

        return memberRepository.findByUser(user)
            .orElseThrow(() -> new MemberNotFoundException("Member not found for authenticated user"));
    }

    public Member findMemberByUser(User user) {
        return memberRepository.findByUser(user)
            .orElseThrow(() -> new MemberNotFoundException("Member not found for user"));
    }
}
