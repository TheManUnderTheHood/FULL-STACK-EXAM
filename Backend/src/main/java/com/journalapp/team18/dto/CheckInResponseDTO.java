package com.eventmanagement.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckInResponseDTO {
    private Boolean success;
    private String message;
    private Long registrationId;
    private String userName;
    private String eventName;
    private Boolean isDuplicate;
    private String timestamp;
}
