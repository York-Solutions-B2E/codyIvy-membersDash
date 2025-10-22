package com.example.benefits.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.benefits.repository.ClaimRepository;
import com.example.benefits.repository.MemberRepository;

import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.benefits.dto.ClaimListRowDTO;
import com.example.benefits.model.Member;
import com.example.benefits.model.Claim;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import java.util.UUID;
import java.util.stream.Collectors;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.data.domain.Sort;


@RestController
@RequestMapping("/api/claims")
public class ClaimsListController {
    private ClaimRepository claimRepository;
    private MemberRepository memberRepository;

    public ClaimsListController(ClaimRepository claimRepository, MemberRepository memberRepository) {
        this.claimRepository = claimRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public Page<ClaimListRowDTO> getClaims(
            @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String email = jwt.getClaim("email");
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        Pageable pageable = PageRequest.of(page, size, Sort.by("claimNumber").descending());
        Page<Claim> claimsPage = claimRepository.findByMember(member, pageable);
        return claimsPage.map(claim -> new ClaimListRowDTO(
                claim.getClaimNumber(),
                claim.getServiceStartDate().toString(),
                claim.getServiceEndDate().toString(),
                claim.getProvider().getName(),
                claim.getStatus().name(),
                claim.getTotalMemberResponsibility().doubleValue()));
    }

}
