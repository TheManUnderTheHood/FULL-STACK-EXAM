package com.eventmanagement.repository;

import com.eventmanagement.model.Event;
import com.eventmanagement.model.User;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends org.springframework.data.mongodb.repository.MongoRepository<Event, String> {
    List<Event> findByOrganizer(User organizer);
    List<Event> findByStatus(Event.EventStatus status);
    List<Event> findByEventNameContainingIgnoreCase(String keyword);
}

