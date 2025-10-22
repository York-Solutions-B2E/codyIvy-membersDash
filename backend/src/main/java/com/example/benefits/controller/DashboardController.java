package com.example.benefits.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.benefits.dto.ActivePlanDTO;
import com.example.benefits.dto.ClaimSummaryDTO;
import com.example.benefits.dto.DashboardDTO;
import com.example.benefits.dto.AccumulatorDTO;
import com.example.benefits.model.Enrollment;
import com.example.benefits.model.Member;
import com.example.benefits.model.Claim;
import com.example.benefits.model.User;
import com.example.benefits.repository.MemberRepository;
import com.example.benefits.repository.UserRepository;
import com.example.benefits.repository.EnrollmentRepository;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.benefits.model.Plan;
import java.security.Principal;
import org.springframework.security.oauth2.jwt.Jwt;
import java.util.List;
import java.util.stream.Collectors;
import com.example.benefits.repository.ClaimRepository;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class DashboardController {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ClaimRepository claimRepository;

    public DashboardController(UserRepository userRepository, MemberRepository memberRepository,
            EnrollmentRepository enrollmentRepository, ClaimRepository claimRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.enrollmentRepository = enrollmentRepository;
        this.claimRepository = claimRepository;
    }

    @GetMapping("dashboard")
    public DashboardDTO getDashboard(@AuthenticationPrincipal Jwt jwt) {

        String email = jwt.getClaim("email");

        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        Member member = memberRepository.findByUser(user).orElseThrow(() -> new RuntimeException("Member not found"));

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
