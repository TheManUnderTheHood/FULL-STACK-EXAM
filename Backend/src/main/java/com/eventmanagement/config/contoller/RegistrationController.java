package com.eventmanagement.controller;

import com.eventmanagement.dto.RegistrationDTO;
import com.eventmanagement.model.Event;
import com.eventmanagement.model.Registration;
import com.eventmanagement.model.User;
import com.eventmanagement.service.EventService;
import com.eventmanagement.service.RegistrationService;
import com.eventmanagement.service.UserService;
import com.eventmanagement.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/registrations")
@CrossOrigin(origins = "*", maxAge = 3600)
public class RegistrationController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/register/{eventId}")
    public ResponseEntity<?> registerForEvent(@PathVariable Long eventId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Event event = eventService.getEventById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));

            // Decrement available seats
            if (!eventService.decrementAvailableSeats(eventId)) {
                return ResponseEntity.badRequest().body("No seats available");
            }

            Registration registration = registrationService.registerUserForEvent(user, event);
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/my-registrations")
    public ResponseEntity<?> getMyRegistrations() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<RegistrationDTO> registrations = registrationService.getUserRegistrations(user);
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getRegistrationById(@PathVariable Long id) {
        try {
            Registration registration = registrationService.getRegistrationById(id)
                    .orElseThrow(() -> new RuntimeException("Registration not found"));
            return ResponseEntity.ok(registration);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/{registrationId}/confirm-payment")
    public ResponseEntity<?> confirmPayment(@PathVariable Long registrationId) {
        try {
            Registration registration = registrationService.getRegistrationById(registrationId)
                    .orElseThrow(() -> new RuntimeException("Registration not found"));

            // Process mock payment
            if (registration.getEvent().getIsPaid()) {
                var payment = paymentService.processMockPayment(
                        registration,
                        registration.getEvent().getTicketPrice()
                );
                registration.setPayment(payment);
            }

            Registration confirmed = registrationService.confirmPayment(registrationId);
            return ResponseEntity.ok(confirmed);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{registrationId}")
    public ResponseEntity<?> cancelRegistration(@PathVariable Long registrationId) {
        try {
            Registration registration = registrationService.getRegistrationById(registrationId)
                    .orElseThrow(() -> new RuntimeException("Registration not found"));

            // Increment available seats back
            eventService.incrementAvailableSeats(registration.getEvent().getId());

            registrationService.cancelRegistration(registrationId);
            return ResponseEntity.ok("Registration cancelled successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
