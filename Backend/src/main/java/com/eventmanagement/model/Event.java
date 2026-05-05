package com.eventmanagement.model;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@org.springframework.data.mongodb.core.mapping.Document

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    
    private String id;

    
    private String eventName;

    
    private String description;

    
    private LocalDateTime eventDate;

    
    private String venue;

    
    private Double ticketPrice;

    
    private Integer totalSeats;

    
    private Integer availableSeats;

    
    private Boolean isPaid;

    private EventStatus status;
    
    private User organizer;

    
    private Set<Registration> registrations = new HashSet<>();

    
    private LocalDateTime createdAt;

    
    private LocalDateTime updatedAt;

    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        status = EventStatus.ACTIVE;
        availableSeats = totalSeats;
    }

    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum EventStatus {
        DRAFT,
        ACTIVE,
        ONGOING,
        COMPLETED,
        CANCELLED
    }
}

