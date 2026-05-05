package com.eventmanagement.controller;

import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.model.Event;
import com.eventmanagement.model.Registration;
import com.eventmanagement.model.User;
import com.eventmanagement.service.EventService;
import com.eventmanagement.service.RegistrationService;
import com.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/organizer/events")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrganizerController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistrationService registrationService;

    @PostMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?> createEvent(@RequestBody Event eventDetails) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User organizer = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Organizer not found"));

            eventDetails.setOrganizer(organizer);
            Event createdEvent = eventService.createEvent(eventDetails);
            return ResponseEntity.ok(createdEvent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?> getMyEvents() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User organizer = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Organizer not found"));

            List<EventDTO> events = eventService.getOrganizerEvents(organizer);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateEvent(@PathVariable String eventId, @RequestBody Event eventDetails) {
        try {
            Event updatedEvent = eventService.updateEvent(eventId, eventDetails);
            return ResponseEntity.ok(updatedEvent);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{eventId}")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteEvent(@PathVariable String eventId) {
        try {
            eventService.deleteEvent(eventId);
            return ResponseEntity.ok("Event deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}/registrations")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?> getEventRegistrations(@PathVariable String eventId) {
        try {
            Event event = eventService.getEventById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            List<Registration> registrations = registrationService.getEventRegistrationsList(event);
            return ResponseEntity.ok(registrations);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{eventId}/export-participants")
    @PreAuthorize("hasRole('ORGANIZER') or hasRole('ADMIN')")
    public ResponseEntity<?> exportParticipantList(@PathVariable String eventId) {
        try {
            Event event = eventService.getEventById(eventId)
                    .orElseThrow(() -> new RuntimeException("Event not found"));
            List<Registration> registrations = registrationService.getEventRegistrationsList(event);

            // Format as CSV
            StringBuilder csvData = new StringBuilder();
            csvData.append("Registration ID,Name,Email,Phone,College,Status,Check-in Status\n");

            for (Registration reg : registrations) {
                csvData.append(String.format("%d,%s,%s,%s,%s,%s,%s\n",
                    reg.getId(),
                    reg.getUser().getFirstName() + " " + reg.getUser().getLastName(),
                    reg.getUser().getEmail(),
                    reg.getUser().getPhoneNumber(),
                    reg.getUser().getCollegeName(),
                    reg.getStatus(),
                    reg.getHasCheckedIn() ? "Checked In" : "Pending"
                ));
            }

            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=participants.csv")
                    .body(csvData.toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

