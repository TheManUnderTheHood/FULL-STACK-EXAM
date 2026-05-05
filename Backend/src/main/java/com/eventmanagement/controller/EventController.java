package com.eventmanagement.controller;

import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.model.Event;
import com.eventmanagement.model.User;
import com.eventmanagement.service.EventService;
import com.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@CrossOrigin(origins = "*", maxAge = 3600)
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<?> getAllEvents() {
        try {
            List<EventDTO> events = eventService.getAllEvents();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/active")
    public ResponseEntity<?> getActiveEvents() {
        try {
            List<EventDTO> events = eventService.getActiveEvents();
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getEventById(@PathVariable String id) {
        try {
            Optional<Event> event = eventService.getEventById(id);
            if (event.isPresent()) {
                return ResponseEntity.ok(event.get());
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchEvents(@RequestParam String keyword) {
        try {
            List<EventDTO> events = eventService.searchEvents(keyword);
            return ResponseEntity.ok(events);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/registrations/count")
    public ResponseEntity<?> getEventRegistrationCount(@PathVariable String id) {
        try {
            Long count = eventService.getEventRegistrationCount(id);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/{id}/checkins/count")
    public ResponseEntity<?> getEventCheckInCount(@PathVariable String id) {
        try {
            Long count = eventService.getEventCheckInCount(id);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

