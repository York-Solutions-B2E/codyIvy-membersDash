package com.example.benefits.repository;

import com.example.benefits.model.Claim;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import com.example.benefits.model.Member;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClaimRepository extends JpaRepository<Claim, UUID> {
    List<Claim> findByMemberId(UUID memberId);

    List<Claim> findByProviderId(UUID providerId);

    Claim findByClaimNumber(String claimNumber);


    List<Claim> findByMember(Member member);
    Page<Claim> findByMember(Member member, Pageable pageable );

    List<Claim> findTop5ByMemberOrderByReceivedDateDesc(Member member);

}