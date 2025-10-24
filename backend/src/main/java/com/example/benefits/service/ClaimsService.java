package com.example.benefits.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.benefits.repository.ClaimRepository;
import com.example.benefits.dto.ClaimListRowDTO;
import com.example.benefits.model.Member;
import com.example.benefits.model.Claim;
import com.example.benefits.dto.ClaimDetailDTO;


@Service
@Transactional(readOnly = true)
public class ClaimsService {
        private final ClaimRepository claimRepository;
        private final MemberLookupService memberLookupService;

        public ClaimsService(ClaimRepository claimRepository, MemberLookupService memberLookupService) {
                this.claimRepository = claimRepository;
                this.memberLookupService = memberLookupService;
        }

        public Page<ClaimListRowDTO> getClaimsList(String email, int page, int size) {
                Member member = memberLookupService.findMemberByEmail(email);

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

        public ClaimDetailDTO getClaimDetail(String email, String claimNumber) {
                Member member = memberLookupService.findMemberByEmail(email);

                Claim claim = claimRepository.findByClaimNumberAndMember(claimNumber, member);
                if (claim == null) {
                        throw new RuntimeException("Claim not found");
                }

                return mapToClaimDetailDTO(claim);

        }

        private ClaimDetailDTO mapToClaimDetailDTO(Claim claim) {
                // Build Provider Summary
                ClaimDetailDTO.ProviderSummaryDTO providerSummary = ClaimDetailDTO.ProviderSummaryDTO.builder()
                                .name(claim.getProvider().getName())
                                .specialty(claim.getProvider().getSpecialty())
                                .build();

                // Build Financial Summary
                ClaimDetailDTO.FinancialSummaryDTO financialSummary = ClaimDetailDTO.FinancialSummaryDTO.builder()
                                .totalBilled(claim.getTotalBilled())
                                .totalAllowed(claim.getTotalAllowed())
                                .totalPaid(claim.getTotalPlanPaid())
                                .memberResponsibility(claim.getTotalMemberResponsibility())
                                .build();

                // Build Claim Lines
                var claimLines = claim.getLines().stream()
                                .map(line -> ClaimDetailDTO.ClaimLineDTO.builder()
                                                .lineNumber(line.getLineNumber())
                                                .cptCode(line.getCptCode())
                                                .description(line.getDescription())
                                                .billedAmount(line.getBilledAmount())
                                                .allowedAmount(line.getAllowedAmount())
                                                .deductibleApplied(line.getDeductibleApplied())
                                                .copayApplied(line.getCopayApplied())
                                                .coinsuranceApplied(line.getCoinsuranceApplied())
                                                .planPaid(line.getPlanPaid())
                                                .memberResponsibility(line.getMemberResponsibility())
                                                .build())
                                .toList();

                // Build Status History
                var statusHistory = claim.getStatusHistory().stream()
                                .map(event -> ClaimDetailDTO.ClaimStatusEventDTO.builder()
                                                .status(event.getStatus())
                                                .occurredAt(event.getOccurredAt())
                                                .note(event.getNote())
                                                .build())
                                .toList();

                return ClaimDetailDTO.builder()
                                .claimNumber(claim.getClaimNumber())
                                .status(claim.getStatus())
                                .serviceStartDate(claim.getServiceStartDate())
                                .serviceEndDate(claim.getServiceEndDate())
                                .provider(providerSummary)
                                .financialSummary(financialSummary)
                                .lineItems(claimLines)
                                .statusHistory(statusHistory)
                                .build();
        }

}
