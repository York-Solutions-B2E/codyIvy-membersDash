package com.example.benefits.service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.benefits.repository.UserRepository;
import com.example.benefits.model.User;

@Service
@RequiredArgsConstructor

public class UserService {

    private final UserRepository userRepository;

    public User findOrCreateUser(String authProvider, String authSub, String email, String name) {
        return userRepository.findByAuthProviderAndAuthSub(authProvider, authSub)
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .authProvider(authProvider)
                            .authSub(authSub)
                            .email(email)
                            .name(name)
                            .build();
                    return userRepository.save(newUser);
                });
    }

}
