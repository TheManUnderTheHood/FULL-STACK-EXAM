package com.eventmanagement.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@org.springframework.data.mongodb.core.mapping.Document

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    @Id
    
    private String id;

    
    private String transactionId;

    
    private Double amount;

    
    
    private PaymentStatus status;

    
    
    private PaymentMethod paymentMethod;

    
    private String gatewayResponse;

    
    private LocalDateTime paidAt;

    
    private LocalDateTime createdAt;

    
    private LocalDateTime updatedAt;

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = PaymentStatus.PENDING;
    }

    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum PaymentStatus {
        PENDING,
        COMPLETED,
        FAILED,
        REFUNDED
    }

    public enum PaymentMethod {
        CREDIT_CARD,
        DEBIT_CARD,
        UPI,
        NETBANKING,
        MOCK
    }
}

