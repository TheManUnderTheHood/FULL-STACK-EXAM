package com.eventmanagement.repository;

import com.eventmanagement.model.Registration;
import com.eventmanagement.model.User;
import com.eventmanagement.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.List;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    Optional<Registration> findByRegistrationToken(String token);
    List<Registration> findByUser(User user);
    List<Registration> findByEvent(Event event);
    List<Registration> findByUserAndEvent(User user, Event event);
    List<Registration> findByEventAndStatus(Event event, Registration.RegistrationStatus status);
    List<Registration> findByEventAndHasCheckedInTrue(Event event);
    Optional<Registration> findByUserAndEvent(User user, Long eventId);
}
