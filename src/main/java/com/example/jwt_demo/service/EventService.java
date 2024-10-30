package com.example.jwt_demo.service;

import com.example.jwt_demo.exceptions.ResourceNotFoundException;
import com.example.jwt_demo.model.Event;
import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.repository.vendor.EventRepository;
import com.example.jwt_demo.repository.vendor.VendorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final VendorRepository vendorRepository;

    public Event create(Event event, Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        event.setVendor(vendor);
        return eventRepository.save(event);
    }

    public Event update(Long id, Event eventDetails, Long vendorId) {
        Event event = eventRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        BeanUtils.copyProperties(eventDetails, event, "id", "vendor", "createdAt");
        return eventRepository.save(event);
    }

    public void delete(Long id, Long vendorId) {
        Event event = eventRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
        eventRepository.delete(event);
    }

    public List<Event> getAllByVendor(Long vendorId) {
        return eventRepository.findByVendorId(vendorId);
    }

    public Event getOne(Long id, Long vendorId) {
        return eventRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));
    }

    public List<Event> getUpcomingEvents(Long vendorId) {
        return eventRepository.findByVendorIdAndIsActive(vendorId, true).stream()
                .filter(event -> event.getStartTime().isAfter(LocalDateTime.now()))
                .collect(Collectors.toList());
    }
}
