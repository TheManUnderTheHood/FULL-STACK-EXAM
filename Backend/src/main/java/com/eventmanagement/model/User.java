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
public class User {
    @Id
    
    private String id;

    
    private String email;

    
    private String password;

    
    private String firstName;

    
    private String lastName;

    
    private String phoneNumber;

    
    private String collegeName;

    
    
    private UserRole role;

    
    private Boolean isActive;

    
    private LocalDateTime createdAt;

    
    private LocalDateTime updatedAt;

    
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum UserRole {
        ADMIN,
        ORGANIZER,
        VOLUNTEER,
        ATTENDEE
    }
}

