package com.eventmanagement.service;

import com.eventmanagement.dto.UserRegistrationDTO;
import com.eventmanagement.model.User;
import com.eventmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(UserRegistrationDTO registrationDTO) {
        if (userRepository.existsByEmail(registrationDTO.getEmail())) {
            throw new RuntimeException("Email already registered");
        }

        User user = User.builder()
                .email(registrationDTO.getEmail())
                .password(passwordEncoder.encode(registrationDTO.getPassword()))
                .firstName(registrationDTO.getFirstName())
                .lastName(registrationDTO.getLastName())
                .phoneNumber(registrationDTO.getPhoneNumber())
                .collegeName(registrationDTO.getCollegeName())
                .role(User.UserRole.ATTENDEE)
                .isActive(true)
                .build();

        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getVolunteers() {
        return userRepository.findByRole(User.UserRole.VOLUNTEER);
    }

    public List<User> getOrganizers() {
        return userRepository.findByRole(User.UserRole.ORGANIZER);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
