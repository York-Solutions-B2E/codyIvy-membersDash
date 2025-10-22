package com.example.benefits.dto;
import lombok.*;




@Data
@NoArgsConstructor
@AllArgsConstructor

public class ClaimListRowDTO {
    private String claimNumber;
    private String serviceStartDate;
    private String serviceEndDate;
    private String providerName;
    private String status;
    private double memberResponsibility;
}