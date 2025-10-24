package com.example.benefits.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;
import org.springframework.security.oauth2.jwt.Jwt;

import com.example.benefits.dto.ClaimListRowDTO;
import com.example.benefits.dto.ClaimDetailDTO;
import com.example.benefits.service.ClaimsService;

@RestController
@RequestMapping("/api/claims")
public class ClaimsListController {
    private final ClaimsService claimsService;

    public ClaimsListController(ClaimsService claimsService) {
        this.claimsService = claimsService;
    }

    @GetMapping
    public Page<ClaimListRowDTO> getClaims(
            @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        String email = jwt.getClaim("email");
        return claimsService.getClaimsList(email, page, size);
    }

    @GetMapping("/{claimNumber}")
    public ClaimDetailDTO getClaimDetail(
            @org.springframework.security.core.annotation.AuthenticationPrincipal Jwt jwt,
            @org.springframework.web.bind.annotation.PathVariable String claimNumber) {
        String email = jwt.getClaim("email");
        return claimsService.getClaimDetail(email, claimNumber);
    }

}
