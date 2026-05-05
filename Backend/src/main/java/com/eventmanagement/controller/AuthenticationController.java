package com.eventmanagement.controller;

import com.eventmanagement.dto.AuthenticationResponseDTO;
import com.eventmanagement.dto.LoginDTO;
import com.eventmanagement.dto.UserRegistrationDTO;
import com.eventmanagement.model.User;
import com.eventmanagement.security.JwtTokenProvider;
import com.eventmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDTO registrationDTO) {
        try {
            User user = userService.registerUser(registrationDTO);
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginDTO loginDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginDTO.getEmail(),
                            loginDTO.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user = userService.getUserByEmail(loginDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            String token = tokenProvider.generateToken(loginDTO.getEmail(), user.getId(), user.getRole().toString());

            AuthenticationResponseDTO response = AuthenticationResponseDTO.builder()
                    .token(token)
                    .userId(user.getId())
                    .email(user.getEmail())
                    .role(user.getRole().toString())
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }
}

