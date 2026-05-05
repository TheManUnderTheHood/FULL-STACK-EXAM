package com.eventmanagement.repository;

import com.eventmanagement.model.Event;
import com.eventmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizer(User organizer);
    List<Event> findByStatus(Event.EventStatus status);
    List<Event> findByEventNameContainingIgnoreCase(String keyword);
}
