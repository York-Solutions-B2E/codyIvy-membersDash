package com.example.benefits.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.benefits.dto.ActivePlanDTO;
import com.example.benefits.dto.DashboardDTO;
import com.example.benefits.model.Enrollment;
import com.example.benefits.model.Member;
import com.example.benefits.model.User;
import com.example.benefits.repository.MemberRepository;
import com.example.benefits.repository.UserRepository;
import com.example.benefits.repository.EnrollmentRepository;
import org.springframework.web.bind.annotation.RequestParam;
import com.example.benefits.model.Plan;
import java.security.Principal;
import org.springframework.security.oauth2.jwt.Jwt;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api")
public class DashboardController {
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
    private final EnrollmentRepository enrollmentRepository;

    public DashboardController(UserRepository userRepository, MemberRepository memberRepository,
            EnrollmentRepository enrollmentRepository) {
        this.userRepository = userRepository;
        this.memberRepository = memberRepository;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("dashboard")
    public DashboardDTO getDashboard(@AuthenticationPrincipal Jwt jwt) {
        System.out.println(jwt.getClaims());
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
        return new DashboardDTO(activePlanDTO);
    }

}
