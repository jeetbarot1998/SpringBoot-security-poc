package com.example.jwt_demo.repository.vendor;
import com.example.jwt_demo.model.Event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByVendorId(Long vendorId);
    List<Event> findByVendorIdAndIsActive(Long vendorId, Boolean isActive);
    List<Event> findByStartTimeAfter(LocalDateTime date);
    Optional<Event> findByIdAndVendorId(Long id, Long vendorId);
}