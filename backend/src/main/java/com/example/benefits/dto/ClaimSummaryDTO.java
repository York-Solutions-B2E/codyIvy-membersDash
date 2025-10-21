package com.example.benefits.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimSummaryDTO {
    private String claimNumber; // e.g., C-10421
    private String status; // e.g., Processed, Denied, Paid, In Review
    private double memberResponsibility;
   
}
