package com.example.benefits.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ActivePlanDTO {
    private String name; // e.g., Gold PPO
    private String networkName; // e.g., Prime
    private int planYear; // e.g., 2025
    private String coverageStart;
    private String coverageEnd;

}
