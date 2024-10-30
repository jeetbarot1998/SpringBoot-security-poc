package com.example.jwt_demo.repository.vendor;

import com.example.jwt_demo.model.Accommodation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface AccommodationRepository extends JpaRepository<Accommodation, Long> {
    List<Accommodation> findByVendorId(Long vendorId);
    List<Accommodation> findByVendorIdAndIsAvailable(Long vendorId, Boolean isAvailable);
    Optional<Accommodation> findByIdAndVendorId(Long id, Long vendorId);
}
