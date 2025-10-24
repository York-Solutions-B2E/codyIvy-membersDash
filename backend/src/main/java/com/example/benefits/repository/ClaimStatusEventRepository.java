package com.example.benefits.repository;

import com.example.benefits.model.Claim;

import com.example.benefits.model.ClaimStatusEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import java.util.List;

public interface ClaimStatusEventRepository extends JpaRepository<ClaimStatusEvent, UUID> {
    // Example: Find all status events for a claim, ordered by occurredAt
    List<ClaimStatusEvent> findByClaimIdOrderByOccurredAtAsc(UUID claimId);

    List<ClaimStatusEvent> findByClaim(Claim claim);
}