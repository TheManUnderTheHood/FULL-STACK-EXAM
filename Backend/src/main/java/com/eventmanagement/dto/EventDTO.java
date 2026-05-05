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
public class EventDTO {
    private String id;
    private String eventName;
    private String description;
    private LocalDateTime eventDate;
    private String venue;
    private Double ticketPrice;
    private Integer totalSeats;
    private Integer availableSeats;
    private Boolean isPaid;
    private String status;
}

