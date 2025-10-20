package com.example.benefits.controller;

import com.example.benefits.model.*;
import com.example.benefits.repository.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

@RestController
@RequestMapping("/api/test-data")
public class TestDataController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private AccumulatorRepository accumulatorRepository;
    @Autowired
    private ProviderRepository providerRepository;
    @Autowired
    private ClaimRepository claimRepository;
    @Autowired
    private ClaimLineRepository claimLineRepository;
    @Autowired
    private ClaimStatusEventRepository claimStatusEventRepository;

    // Get ALL data for a user by their email
    @GetMapping("/user/{email}")
    public Map<String, Object> getAllDataForUser(@PathVariable String email) {
        Map<String, Object> result = new LinkedHashMap<>();

        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return Map.of("error", "User not found: " + email);
        }
        User user = userOpt.get();
        result.put("user", user);

        Optional<Member> memberOpt = memberRepository.findByUser(user);
        if (memberOpt.isPresent()) {
            Member member = memberOpt.get();
            result.put("member", member);

            List<Enrollment> enrollments = enrollmentRepository.findByMember(member);
            result.put("enrollments", enrollments);

            List<Claim> claims = claimRepository.findByMember(member);
            result.put("claims", claims);

            // For each enrollment
            List<Map<String, Object>> accumulatorsData = new ArrayList<>();
            for (Enrollment e : enrollments) {
                List<Accumulator> accumulators = accumulatorRepository.findByEnrollment(e);
                accumulatorsData.add(Map.of("enrollmentId", e.getId(), "accumulators", accumulators));
            }
            result.put("accumulators", accumulatorsData);

            // For each claim: lines and status history
            List<Map<String, Object>> claimsExtra = new ArrayList<>();
            for (Claim claim : claims) {
                List<ClaimLine> lines = claimLineRepository.findByClaim(claim);
                List<ClaimStatusEvent> statusEvents = claimStatusEventRepository.findByClaim(claim);
                claimsExtra.add(Map.of(
                    "claimId", claim.getId(),
                    "lines", lines,
                    "statusEvents", statusEvents
                ));
            }
            result.put("claim_lines_and_status_events", claimsExtra);
        }

        // Everything else (all plans, all providers)
        result.put("plans", planRepository.findAll());
        result.put("providers", providerRepository.findAll());

        return result;
    }
}