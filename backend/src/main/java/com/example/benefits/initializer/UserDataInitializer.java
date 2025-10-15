package com.example.benefits.initializer;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import com.example.benefits.model.User;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import com.example.benefits.repository.UserRepository;


@Component
@RequiredArgsConstructor
public class UserDataInitializer implements ApplicationRunner {
    private final UserRepository userRepository;

    @Override
    public void run(ApplicationArguments args) {
        User user = User.builder()
                .authProvider("google")
                .authSub("google-sub-123")
                .email("test@example.com")
                .name("Test User")
                .build();
        userRepository.save(user);
        System.out.println("User saved: " + user);
    }
}
