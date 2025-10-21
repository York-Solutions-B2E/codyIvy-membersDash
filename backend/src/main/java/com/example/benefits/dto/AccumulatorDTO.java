package com.example.benefits.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccumulatorDTO {
    private String type; // e.g., "DEDUCTIBLE" or "OOP_MAX"
    private String tier; // e.g., "IN_NETWORK"
    private double usedAmount;
    private double limitAmount;
    
}