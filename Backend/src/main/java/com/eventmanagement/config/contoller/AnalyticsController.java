package com.eventmanagement.controller;

import com.eventmanagement.dto.EventAnalyticsDTO;
import com.eventmanagement.model.User;
import com.eventmanagement.service.AnalyticsService;
import com.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/analytics")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AnalyticsController {

    @Autowired
    private AnalyticsService analyticsService;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();
            dashboard.put("totalRegistrations", analyticsService.getGlobalTotalRegistrations());
            dashboard.put("totalCheckIns", analyticsService.getGlobalTotalCheckIns());
            dashboard.put("totalRevenue", analyticsService.getGlobalTotalRevenue());
            dashboard.put("totalEvents", analyticsService.getGlobalTotalEvents());
            dashboard.put("activeEvents", analyticsService.getActiveEventsCount());

            return ResponseEntity.ok(dashboard);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAllEventsAnalytics() {
        try {
            List<EventAnalyticsDTO> analytics = analyticsService.getAllEventsAnalytics();
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/events/{eventId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ORGANIZER')")
    public ResponseEntity<?> getEventAnalytics(@PathVariable Long eventId) {
        try {
            // For organizers, verify they own the event
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.getAuthorities().stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ORGANIZER"))) {
                String email = authentication.getName();
                User user = userService.getUserByEmail(email).get();
                EventAnalyticsDTO analytics = analyticsService.getOrganizerEventAnalytics(eventId, user.getId());
                return ResponseEntity.ok(analytics);
            }

            // For admins
            EventAnalyticsDTO analytics = analyticsService.getEventAnalytics(eventId);
            return ResponseEntity.ok(analytics);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
