package com.example.benefits.repository;

import com.example.benefits.model.ClaimLine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;
import com.example.benefits.model.Claim;

public interface ClaimLineRepository extends JpaRepository<ClaimLine, UUID> {
    List<ClaimLine> findByClaimId(UUID claimId);
    List<ClaimLine> findByClaim(Claim claim);
}