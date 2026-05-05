package com.eventmanagement.repository;

import com.eventmanagement.model.CheckIn;
import com.eventmanagement.model.Registration;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface CheckInRepository extends org.springframework.data.mongodb.repository.MongoRepository<CheckIn, String> {
    Optional<CheckIn> findByRegistration(Registration registration);
    List<CheckIn> findByRegistrationEvent(String eventId);
    boolean existsByRegistration(Registration registration);
}

