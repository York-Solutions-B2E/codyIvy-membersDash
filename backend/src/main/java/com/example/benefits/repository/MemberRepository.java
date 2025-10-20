package com.example.benefits.repository;

import com.example.benefits.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.Optional;
import com.example.benefits.model.User;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Optional<Member> findByUser(User user);
}