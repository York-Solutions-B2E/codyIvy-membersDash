package com.example.benefits.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "plans")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plan {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String name;          // e.g., "Gold PPO"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PlanType type;        // PPO/HMO/...

    private String networkName;   // e.g., "Prime"

    @Column(nullable = false)
    private Integer planYear;     // e.g., 2025
}