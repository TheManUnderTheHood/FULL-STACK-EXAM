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
public class CheckIn {
    @Id
    
    private String id;
    
    private Registration registration;
    
    private User volunteer;

    
    private LocalDateTime checkedInAt;

    
    private LocalDateTime qrVerifiedAt;

    
    private Boolean isValid;

    
    private String remarks;

    
    private LocalDateTime createdAt;

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (checkedInAt == null) {
            checkedInAt = LocalDateTime.now();
        }
        if (qrVerifiedAt == null) {
            qrVerifiedAt = LocalDateTime.now();
        }
    }
}

