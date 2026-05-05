package com.eventmanagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "check_ins")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CheckIn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "registration_id", nullable = false)
    private Registration registration;

    @ManyToOne
    @JoinColumn(name = "volunteer_id", nullable = false)
    private User volunteer;

    @Column(name = "checked_in_at", nullable = false)
    private LocalDateTime checkedInAt;

    @Column(name = "qr_verified_at")
    private LocalDateTime qrVerifiedAt;

    @Column(nullable = false)
    private Boolean isValid;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (checkedInAt == null) {
            checkedInAt = LocalDateTime.now();
        }
        if (qrVerifiedAt == null) {
            qrVerifiedAt = LocalDateTime.now();
        }
    }
}
