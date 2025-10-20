package com.example.benefits.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "accumulators")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Accumulator {
    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AccumulatorType type;      // DEDUCTIBLE or OOP_MAX

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NetworkTier tier;          // IN_NETWORK/OUT_OF_NETWORK

    @Column(nullable = false)
    private BigDecimal limitAmount;    // e.g., 1500.00

    @Column(nullable = false)
    private BigDecimal usedAmount;     // e.g., 300.00
}