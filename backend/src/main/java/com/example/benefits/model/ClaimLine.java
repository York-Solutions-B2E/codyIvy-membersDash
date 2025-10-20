package com.example.benefits.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "claim_lines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClaimLine {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "claim_id", nullable = false)
    private Claim claim;

    @Column(nullable = false)
    private Integer lineNumber; // 1..n

    @Column(nullable = false)
    private String cptCode; // e.g., "99213"

    private String description;

    @Column(nullable = false)
    private BigDecimal billedAmount;

    @Column(nullable = false)
    private BigDecimal allowedAmount;

    @Column(nullable = false)
    private BigDecimal deductibleApplied;

    @Column(nullable = false)
    private BigDecimal copayApplied;

    @Column(nullable = false)
    private BigDecimal coinsuranceApplied;

    @Column(nullable = false)
    private BigDecimal planPaid;

    @Column(nullable = false)
    private BigDecimal memberResponsibility;
}