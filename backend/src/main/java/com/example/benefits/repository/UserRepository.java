package com.example.benefits.repository;

import com.example.benefits.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;


public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByAuthSub(String authSub);
    Optional<User> findByAuthProviderAndAuthSub(String authProvider, String authSub);
    
}
