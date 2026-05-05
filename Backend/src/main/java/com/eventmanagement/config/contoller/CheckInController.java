package com.eventmanagement.controller;

import com.eventmanagement.dto.CheckInResponseDTO;
import com.eventmanagement.model.User;
import com.eventmanagement.service.CheckInService;
import com.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/volunteer/checkin")
@CrossOrigin(origins = "*", maxAge = 3600)
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @Autowired
    private UserService userService;

    @PostMapping("/scan")
    public ResponseEntity<?> scanQRCode(@RequestParam String qrCode) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User volunteer = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Volunteer not found"));

            // Verify volunteer role
            if (!volunteer.getRole().equals(User.UserRole.VOLUNTEER) && 
                !volunteer.getRole().equals(User.UserRole.ADMIN)) {
                return ResponseEntity.badRequest().body("Only volunteers can check in attendees");
            }

            CheckInResponseDTO response = checkInService.processQRCodeScan(qrCode, volunteer);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/manual-search")
    public ResponseEntity<?> manualCheckIn(@RequestParam String searchParam) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String email = authentication.getName();

            User volunteer = userService.getUserByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Volunteer not found"));

            // Verify volunteer role
            if (!volunteer.getRole().equals(User.UserRole.VOLUNTEER) && 
                !volunteer.getRole().equals(User.UserRole.ADMIN)) {
                return ResponseEntity.badRequest().body("Only volunteers can check in attendees");
            }

            CheckInResponseDTO response = checkInService.manualCheckInBySearchParam(searchParam, volunteer);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/status/{registrationToken}")
    public ResponseEntity<?> getCheckInStatus(@PathVariable String registrationToken) {
        try {
            boolean isCheckedIn = checkInService.isAlreadyCheckedIn(registrationToken);
            return ResponseEntity.ok("isCheckedIn: " + isCheckedIn);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}
