package com.eventmanagement.repository;

import com.eventmanagement.model.CheckIn;
import com.eventmanagement.model.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRepository extends JpaRepository<CheckIn, Long> {
    Optional<CheckIn> findByRegistration(Registration registration);
    List<CheckIn> findByRegistrationEvent(Long eventId);
    boolean existsByRegistration(Registration registration);
}
