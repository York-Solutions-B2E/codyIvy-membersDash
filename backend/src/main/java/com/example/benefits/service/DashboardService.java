package com.example.benefits.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.benefits.repository.ClaimRepository;
import com.example.benefits.dto.AccumulatorDTO;
import com.example.benefits.dto.ActivePlanDTO;
import com.example.benefits.dto.ClaimSummaryDTO;
import com.example.benefits.dto.DashboardDTO;
import com.example.benefits.model.Enrollment;
import com.example.benefits.model.Member;
import com.example.benefits.model.Plan;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ClaimRepository claimRepository;
    private final MemberLookupService memberLookupService;

    public DashboardService(ClaimRepository claimRepository, MemberLookupService memberLookupService) {
        this.memberLookupService = memberLookupService;
        this.claimRepository = claimRepository;
    }

    public DashboardDTO getDashboardData(String email) {

        Member member = memberLookupService.findMemberByEmail(email);

        Enrollment activeEnrollment = member.getEnrollments().stream()
                .filter(Enrollment::getActive)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Active enrollment not found"));

        Plan plan = activeEnrollment.getPlan();

        ActivePlanDTO activePlanDTO = new ActivePlanDTO(
                plan.getName(),
                plan.getNetworkName(),
                plan.getPlanYear(),
                activeEnrollment.getCoverageStart().toString(),
                activeEnrollment.getCoverageEnd().toString());

        List<AccumulatorDTO> accumulatorDTOs = activeEnrollment.getAccumulators().stream()
                .map(acc -> new AccumulatorDTO(
                        acc.getType().name(),
                        acc.getTier().name(),
                        acc.getUsedAmount().doubleValue(),
                        acc.getLimitAmount().doubleValue()))
                .collect(Collectors.toList());

        List<ClaimSummaryDTO> recentClaims = claimRepository.findTop5ByMemberOrderByReceivedDateDesc(member).stream()
                .map(claim -> new ClaimSummaryDTO(
                        claim.getClaimNumber(),
                        claim.getStatus().name(),
                        claim.getTotalMemberResponsibility().doubleValue()))
                .collect(Collectors.toList());

        return new DashboardDTO(activePlanDTO, accumulatorDTOs, recentClaims);
    }
}
