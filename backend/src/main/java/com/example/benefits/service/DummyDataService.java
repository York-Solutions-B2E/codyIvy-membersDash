package com.example.benefits.service;

import com.example.benefits.repository.*;
import com.example.benefits.model.*;

import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class DummyDataService {
        private final PlanRepository planRepository;
        private final EnrollmentRepository enrollmentRepository;
        private final AccumulatorRepository accumulatorRepository;
        private final ProviderRepository providerRepository;
        private final ClaimRepository claimRepository;
        private final ClaimLineRepository claimLineRepository;
        private final ClaimStatusEventRepository claimStatusEventRepository;

        public DummyDataService(PlanRepository planRepository,
                        EnrollmentRepository enrollmentRepository,
                        AccumulatorRepository accumulatorRepository,
                        ProviderRepository providerRepository,
                        ClaimRepository claimRepository,
                        ClaimLineRepository claimLineRepository,
                        ClaimStatusEventRepository claimStatusEventRepository) {
                this.planRepository = planRepository;
                this.enrollmentRepository = enrollmentRepository;
                this.accumulatorRepository = accumulatorRepository;
                this.providerRepository = providerRepository;
                this.claimRepository = claimRepository;
                this.claimLineRepository = claimLineRepository;
                this.claimStatusEventRepository = claimStatusEventRepository;
        }

        public void ensureDummyDataForMember(Member member) {
                if (claimRepository.countByMember(member) == 0) {
                        generateDummyData(member);
                }
        }

        @Transactional
        public void generateDummyData(Member member) {
                // 1. PLAN
                Plan plan = Plan.builder()
                                .name("Gold PPO")
                                .type(PlanType.PPO)
                                .networkName("Prime")
                                .planYear(LocalDate.now().getYear())
                                .build();
                plan = planRepository.save(plan);

                // 2. ENROLLMENT
                Enrollment enrollment = Enrollment.builder()
                                .member(member)
                                .plan(plan)
                                .coverageStart(LocalDate.of(LocalDate.now().getYear(), 1, 1))
                                .coverageEnd(LocalDate.of(LocalDate.now().getYear(), 12, 31))
                                .active(true)
                                .build();
                enrollment = enrollmentRepository.save(enrollment);

                // 3. ACCUMULATORS
                Accumulator accumulator = Accumulator.builder()
                                .enrollment(enrollment)
                                .type(AccumulatorType.DEDUCTIBLE)
                                .tier(NetworkTier.IN_NETWORK)
                                .limitAmount(new java.math.BigDecimal("1500.00"))
                                .usedAmount(new java.math.BigDecimal("300.00"))
                                .build();
                accumulatorRepository.save(accumulator);

                // 4. PROVIDERS
                Provider provider = Provider.builder()
                                .name("HealthCare Clinic")
                                .address("123 Main St, Anytown, USA")
                                .specialty("Heart")
                                .phoneNumber("555-123-4567")
                                .build();
                provider = providerRepository.save(provider);

                // 5. CLAIMS
                ClaimStatus[] statuses = ClaimStatus.values();
                Random random = new Random();
                List<Claim> claims = new ArrayList<>();
                
                for (int i = 1; i <= 8; i++) {
                        ClaimStatus randomStatus = statuses[random.nextInt(statuses.length)];
                        
                        Claim claim = Claim.builder()
                                        .claimNumber(String.format("CLM-%04d", i))
                                        .member(member)
                                        .provider(provider)
                                        .serviceStartDate(LocalDate.now().minusDays(10L * (9 - i)))
                                        .serviceEndDate(LocalDate.now().minusDays(10L * (9 - i) - 5))
                                        .receivedDate(LocalDate.now().minusDays(10L * (9 - i) - 3))
                                        .status(randomStatus)
                                        .totalBilled(new BigDecimal(1000 + 100 * i + ".00"))
                                        .totalAllowed(new BigDecimal(800 + 50 * i + ".00"))
                                        .totalPlanPaid(new BigDecimal(600 + 20 * i + ".00"))
                                        .totalMemberResponsibility(new BigDecimal(200 + 10 * i + ".00"))
                                        .updatedAt(OffsetDateTime.now().minusDays(i))
                                        .build();
                        claim = claimRepository.save(claim);
                        claims.add(claim);

                        // 6. CLAIM LINES (2-3 per claim)
                        String[][] cptData = {
                                {"99213", "Office Visit, Established Patient", "150.00", "120.00"},
                                {"81002", "Urinalysis", "50.00", "40.00"},
                                {"85025", "Blood Count", "75.00", "60.00"},
                                {"80053", "Comprehensive Metabolic Panel", "120.00", "95.00"},
                                {"93000", "Electrocardiogram", "100.00", "80.00"},
                                {"36415", "Blood Draw", "25.00", "20.00"}
                        };
                        
                        int numLines = 2 + random.nextInt(2); // 2 or 3 lines per claim
                        
                        for (int lineNum = 1; lineNum <= numLines; lineNum++) {
                                String[] selectedCpt = cptData[random.nextInt(cptData.length)];
                                
                                BigDecimal billed = new BigDecimal(selectedCpt[2]);
                                BigDecimal allowed = new BigDecimal(selectedCpt[3]);
                                BigDecimal deductible = new BigDecimal(random.nextInt(21) + ".00"); // 0-20
                                BigDecimal copay = new BigDecimal(random.nextInt(26) + ".00"); // 0-25
                                BigDecimal coinsurance = new BigDecimal(random.nextInt(16) + ".00"); // 0-15
                                BigDecimal planPaid = allowed.subtract(deductible).subtract(copay).subtract(coinsurance);
                                BigDecimal memberResp = deductible.add(copay).add(coinsurance);
                                
                                ClaimLine line = ClaimLine.builder()
                                                .claim(claim)
                                                .lineNumber(lineNum)
                                                .cptCode(selectedCpt[0])
                                                .description(selectedCpt[1])
                                                .billedAmount(billed)
                                                .allowedAmount(allowed)
                                                .deductibleApplied(deductible)
                                                .copayApplied(copay)
                                                .coinsuranceApplied(coinsurance)
                                                .planPaid(planPaid)
                                                .memberResponsibility(memberResp)
                                                .build();
                                claimLineRepository.save(line);
                        }

                        // 7. CLAIM STATUS EVENTS
                        ClaimStatusEvent event = ClaimStatusEvent.builder()
                                        .claim(claim)
                                        .status(ClaimStatus.SUBMITTED)
                                        .occurredAt(OffsetDateTime.now().minusDays(i))
                                        .note("Claim " + claim.getClaimNumber() + " submitted for processing")
                                        .build();
                        claimStatusEventRepository.save(event);
                }
        }
}