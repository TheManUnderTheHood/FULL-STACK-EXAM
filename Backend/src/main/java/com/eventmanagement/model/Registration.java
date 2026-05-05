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
public class Registration {
    @Id
    
    private String id;

    
    private User user;

    
    private Event event;

    
    private String registrationToken;  // Unique encrypted token for QR code

    
    private String qrCodePath;  // Path or base64 of QR code image

    
    
    private RegistrationStatus status;

    
    private Payment payment;

    
    private Boolean hasCheckedIn;

    
    private LocalDateTime checkedInAt;

    
    private LocalDateTime createdAt;

    
    private LocalDateTime updatedAt;

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        hasCheckedIn = false;
        status = RegistrationStatus.PENDING;
    }

    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum RegistrationStatus {
        PENDING,
        CONFIRMED,
        CANCELLED,
        CHECKED_IN
    }
}

