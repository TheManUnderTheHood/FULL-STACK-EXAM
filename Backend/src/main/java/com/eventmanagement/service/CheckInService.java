package com.eventmanagement.service;

import com.eventmanagement.dto.CheckInResponseDTO;
import com.eventmanagement.model.CheckIn;
import com.eventmanagement.model.Registration;
import com.eventmanagement.model.User;
import com.eventmanagement.repository.CheckInRepository;
import com.eventmanagement.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class CheckInService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private CheckInRepository checkInRepository;

    /**
     * Process QR code scan and check in the attendee
     * Prevents duplicate entries with the same QR code
     */
    public CheckInResponseDTO processQRCodeScan(String registrationToken, User volunteer) {
        CheckInResponseDTO response = new CheckInResponseDTO();
        response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        try {
            // Find registration by token
            Optional<Registration> registrationOptional = registrationRepository.findByRegistrationToken(registrationToken);

            if (!registrationOptional.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Invalid QR Code - Registration not found");
                response.setIsDuplicate(false);
                return response;
            }

            Registration registration = registrationOptional.get();

            // Check if already checked in (DUPLICATE ENTRY PREVENTION)
            if (registration.getHasCheckedIn()) {
                response.setSuccess(false);
                response.setMessage("⚠️ DUPLICATE ENTRY DETECTED! This pass has already been used.");
                response.setIsDuplicate(true);
                response.setRegistrationId(registration.getId());
                response.setUserName(registration.getUser().getFirstName() + " " + registration.getUser().getLastName());
                response.setEventName(registration.getEvent().getEventName());
                return response;
            }

            // Check registration status
            if (!registration.getStatus().equals(Registration.RegistrationStatus.CONFIRMED) &&
                !registration.getStatus().equals(Registration.RegistrationStatus.PENDING)) {
                response.setSuccess(false);
                response.setMessage("Registration is not valid - Status: " + registration.getStatus());
                response.setIsDuplicate(false);
                return response;
            }

            // Mark as checked in
            registration.setHasCheckedIn(true);
            registration.setCheckedInAt(LocalDateTime.now());
            registration.setStatus(Registration.RegistrationStatus.CHECKED_IN);
            registrationRepository.save(registration);

            // Record check-in
            CheckIn checkIn = CheckIn.builder()
                    .registration(registration)
                    .volunteer(volunteer)
                    .checkedInAt(LocalDateTime.now())
                    .qrVerifiedAt(LocalDateTime.now())
                    .isValid(true)
                    .build();
            checkInRepository.save(checkIn);

            // Success response
            response.setSuccess(true);
            response.setMessage("✓ Check-in Successful!");
            response.setRegistrationId(registration.getId());
            response.setUserName(registration.getUser().getFirstName() + " " + registration.getUser().getLastName());
            response.setEventName(registration.getEvent().getEventName());
            response.setIsDuplicate(false);

            return response;

        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("Error processing check-in: " + e.getMessage());
            response.setIsDuplicate(false);
            return response;
        }
    }

    /**
     * Search attendee by ID or phone number (fallback if scanning fails)
     */
    public CheckInResponseDTO manualCheckInBySearchParam(String searchParam, User volunteer) {
        CheckInResponseDTO response = new CheckInResponseDTO();
        response.setTimestamp(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        try {
            // Try to find by registration ID
            Optional<Registration> registrationOptional = registrationRepository.findById(String.valueOf(Long.parseLong(searchParam)));

            if (!registrationOptional.isPresent()) {
                response.setSuccess(false);
                response.setMessage("Attendee not found with ID: " + searchParam);
                return response;
            }

            // Use the same QR scanning logic
            return processQRCodeScan(registrationOptional.get().getRegistrationToken(), volunteer);

        } catch (NumberFormatException e) {
            response.setSuccess(false);
            response.setMessage("Invalid search parameter");
            return response;
        }
    }

    /**
     * Get check-in status for a registration
     */
    public boolean isAlreadyCheckedIn(String registrationToken) {
        Optional<Registration> registration = registrationRepository.findByRegistrationToken(registrationToken);
        if (registration.isPresent()) {
            return registration.get().getHasCheckedIn();
        }
        return false;
    }

    /**
     * Get check-in count for an event
     */
    public long getEventCheckInCount(String eventId) {
        return checkInRepository.findByRegistrationEvent(eventId).size();
    }
}

