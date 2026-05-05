package com.eventmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EventAnalyticsDTO {
    private String eventId;
    private String eventName;
    private Integer totalRegistrations;
    private Integer totalCheckIns;
    private Integer pendingRegistrations;
    private Integer totalSeats;
    private Integer availableSeats;
    private Double ticketPrice;
    private Double totalRevenue;
    private Double checkInPercentage;
    private String eventStatus;
}

