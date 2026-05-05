package com.eventmanagement.service;

import com.eventmanagement.dto.EventAnalyticsDTO;
import com.eventmanagement.model.Event;
import com.eventmanagement.model.Registration;
import com.eventmanagement.repository.EventRepository;
import com.eventmanagement.repository.RegistrationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private RegistrationRepository registrationRepository;

    @Autowired
    private PaymentService paymentService;

    public EventAnalyticsDTO getEventAnalytics(String eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        List<Registration> registrations = registrationRepository.findByEvent(event);
        long totalRegistrations = registrations.size();
        long checkedInCount = registrations.stream()
                .filter(Registration::getHasCheckedIn)
                .count();

        long pendingPayments = registrations.stream()
                .filter(reg -> reg.getPayment() == null)
                .count();

        Double totalRevenue = registrations.stream()
                .filter(reg -> reg.getPayment() != null)
                .mapToDouble(reg -> reg.getPayment().getAmount())
                .sum();

        Double checkInPercentage = totalRegistrations > 0 ? (checkedInCount * 100.0 / totalRegistrations) : 0.0;

        return EventAnalyticsDTO.builder()
                .eventId(eventId)
                .eventName(event.getEventName())
                .totalRegistrations((int) totalRegistrations)
                .totalCheckIns((int) checkedInCount)
                .pendingRegistrations((int) pendingPayments)
                .totalSeats(event.getTotalSeats())
                .availableSeats(event.getAvailableSeats())
                .ticketPrice(event.getTicketPrice())
                .totalRevenue(totalRevenue)
                .checkInPercentage(Math.round(checkInPercentage * 100.0) / 100.0)
                .eventStatus(event.getStatus().toString())
                .build();
    }

    public List<EventAnalyticsDTO> getAllEventsAnalytics() {
        return eventRepository.findAll().stream()
                .map(event -> getEventAnalytics(event.getId()))
                .collect(Collectors.toList());
    }

    public EventAnalyticsDTO getOrganizerEventAnalytics(String eventId, String organizerId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        if (!event.getOrganizer().getId().equals(organizerId)) {
            throw new RuntimeException("Unauthorized access to event analytics");
        }

        return getEventAnalytics(eventId);
    }

    public Long getGlobalTotalRegistrations() {
        return registrationRepository.count();
    }

    public Long getGlobalTotalCheckIns() {
        return registrationRepository.findAll().stream()
                .filter(Registration::getHasCheckedIn)
                .count();
    }

    public Double getGlobalTotalRevenue() {
        return paymentService.getTotalRevenue();
    }

    public Long getGlobalTotalEvents() {
        return eventRepository.count();
    }

    public Long getActiveEventsCount() {
        return (long) eventRepository.findByStatus(Event.EventStatus.ACTIVE).size();
    }
}

