package com.example.benefits.dto;
import com.example.benefits.model.ClaimStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;




@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClaimDetailDTO {
    private String claimNumber;
    private ClaimStatus status;
    private LocalDate serviceStartDate;
    private LocalDate serviceEndDate;
    private ProviderSummaryDTO provider;
    private FinancialSummaryDTO financialSummary;
    private List<ClaimLineDTO> lineItems;
    private List<ClaimStatusEventDTO> statusHistory;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProviderSummaryDTO {
        private String name;
        private String specialty;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FinancialSummaryDTO {
        private BigDecimal totalBilled;
        private BigDecimal totalAllowed;
        private BigDecimal totalPaid;
        private BigDecimal memberResponsibility;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClaimLineDTO {
        private Integer lineNumber;
        private String cptCode;
        private String description;
        private BigDecimal billedAmount;
        private BigDecimal allowedAmount;
        private BigDecimal deductibleApplied;
        private BigDecimal copayApplied;
        private BigDecimal coinsuranceApplied;
        private BigDecimal planPaid;
        private BigDecimal memberResponsibility;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClaimStatusEventDTO {
        private ClaimStatus status;
        private OffsetDateTime occurredAt;
        private String note;
    }

}
