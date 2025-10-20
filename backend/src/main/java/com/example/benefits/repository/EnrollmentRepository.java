package com.example.benefits.repository;
import com.example.benefits.model.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import com.example.benefits.model.Member;

public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {
    List<Enrollment> findByMemberId(UUID memberId);
    List<Enrollment> findByMember(Member member);
    
}
