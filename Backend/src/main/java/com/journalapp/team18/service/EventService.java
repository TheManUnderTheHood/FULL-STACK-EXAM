package com.eventmanagement.service;

import com.eventmanagement.dto.EventDTO;
import com.eventmanagement.model.Event;
import com.eventmanagement.model.User;
import com.eventmanagement.repository.EventRepository;
import com.eventmanagement.repository.RegistrationRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private ModelMapper modelMapper;

    public Event createEvent(Event event) {
        event.setAvailableSeats(event.getTotalSeats());
        return eventRepository.save(event);
    }

    public Optional<Event> getEventById(Long id) {
        return eventRepository.findById(id);
    }

    public List<EventDTO> getAllEvents() {
        return eventRepository.findAll().stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    public List<EventDTO> getActiveEvents() {
        return eventRepository.findByStatus(Event.EventStatus.ACTIVE).stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    public List<EventDTO> searchEvents(String keyword) {
        return eventRepository.findByEventNameContainingIgnoreCase(keyword).stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    public List<EventDTO> getOrganizerEvents(User organizer) {
        return eventRepository.findByOrganizer(organizer).stream()
                .map(event -> modelMapper.map(event, EventDTO.class))
                .collect(Collectors.toList());
    }

    public Event updateEvent(Long id, Event eventDetails) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        event.setEventName(eventDetails.getEventName());
        event.setDescription(eventDetails.getDescription());
        event.setEventDate(eventDetails.getEventDate());
        event.setVenue(eventDetails.getVenue());
        event.setTicketPrice(eventDetails.getTicketPrice());
        event.setStatus(eventDetails.getStatus());

        return eventRepository.save(event);
    }

    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    public Long getEventRegistrationCount(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return registrationRepository.findByEvent(event).stream()
                .filter(reg -> reg.getStatus() != null && 
                        !reg.getStatus().equals(com.eventmanagement.model.Registration.RegistrationStatus.CANCELLED))
                .count();
    }

    public Long getEventCheckInCount(Long eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        return registrationRepository.findByEventAndHasCheckedInTrue(event).size();
    }

    public boolean decrementAvailableSeats(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getAvailableSeats() > 0) {
                event.setAvailableSeats(event.getAvailableSeats() - 1);
                eventRepository.save(event);
                return true;
            }
        }
        return false;
    }

    public boolean incrementAvailableSeats(Long eventId) {
        Optional<Event> eventOptional = eventRepository.findById(eventId);
        if (eventOptional.isPresent()) {
            Event event = eventOptional.get();
            if (event.getAvailableSeats() < event.getTotalSeats()) {
                event.setAvailableSeats(event.getAvailableSeats() + 1);
                eventRepository.save(event);
                return true;
            }
        }
        return false;
    }
}
