package com.example.benefits.dto;

import java.util.List;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashboardDTO {
    private ActivePlanDTO activePlan;
    private List<AccumulatorDTO> accumulators;
    // private List<ClaimSummaryDTO> recentClaims;

}