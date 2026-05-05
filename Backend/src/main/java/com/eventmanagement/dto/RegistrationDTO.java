package com.eventmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegistrationDTO {
    private String id;
    private String eventId;
    private String registrationToken;
    private String qrCodePath;
    private String status;
    private Boolean hasCheckedIn;
    private LocalDateTime checkedInAt;
    private PaymentDTO payment;
    private LocalDateTime createdAt;
}

