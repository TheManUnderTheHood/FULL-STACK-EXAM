package com.eventmanagement.service;

import com.eventmanagement.dto.RegistrationDTO;
import com.eventmanagement.model.Event;
import com.eventmanagement.model.Registration;
import com.eventmanagement.model.User;
import com.eventmanagement.repository.RegistrationRepository;
import com.google.zxing.WriterException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RegistrationService {

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private QRCodeService qrCodeService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private ModelMapper modelMapper;

    public Registration registerUserForEvent(User user, Event event) throws WriterException, IOException {
        // Check if user is already registered
        List<Registration> existingRegistrations = registrationRepository.findByUserAndEvent(user, event);
        if (!existingRegistrations.isEmpty()) {
            throw new RuntimeException("User is already registered for this event");
        }

        // Check seat availability
        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available for this event");
        }

        // Generate unique token for QR code
        String registrationToken = qrCodeService.generateUniqueToken();

        // Generate QR code (Base64 encoded)
        String qrCodeBase64 = qrCodeService.generateQRCodeBase64(registrationToken, 300, 300);

        // Create registration
        Registration registration = Registration.builder()
                .user(user)
                .event(event)
                .registrationToken(registrationToken)
                .qrCodePath(qrCodeBase64)
                .status(Registration.RegistrationStatus.PENDING)
                .hasCheckedIn(false)
                .build();

        registration = registrationRepository.save(registration);

        // Send email with ticket and QR code
        emailService.sendTicketWithQRCode(
                user.getEmail(),
                user.getFirstName() + " " + user.getLastName(),
                event.getEventName(),
                qrCodeBase64,
                registrationToken
        );

        return registration;
    }

    public Registration getRegistrationByToken(String token) {
        return registrationRepository.findByRegistrationToken(token)
                .orElseThrow(() -> new RuntimeException("Registration not found"));
    }

    public Optional<Registration> getRegistrationById(Long id) {
        return registrationRepository.findById(id);
    }

    public List<RegistrationDTO> getUserRegistrations(User user) {
        return registrationRepository.findByUser(user).stream()
                .map(reg -> modelMapper.map(reg, RegistrationDTO.class))
                .collect(Collectors.toList());
    }

    public List<RegistrationDTO> getEventRegistrations(Event event) {
        return registrationRepository.findByEvent(event).stream()
                .map(reg -> modelMapper.map(reg, RegistrationDTO.class))
                .collect(Collectors.toList());
    }

    public Registration confirmPayment(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registration.setStatus(Registration.RegistrationStatus.CONFIRMED);
        return registrationRepository.save(registration);
    }

    public void cancelRegistration(Long registrationId) {
        Registration registration = registrationRepository.findById(registrationId)
                .orElseThrow(() -> new RuntimeException("Registration not found"));

        registration.setStatus(Registration.RegistrationStatus.CANCELLED);
        registrationRepository.save(registration);
    }

    public List<Registration> getEventRegistrationsList(Event event) {
        return registrationRepository.findByEvent(event);
    }
}
