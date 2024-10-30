package com.example.jwt_demo.repository.vendor;

import com.example.jwt_demo.model.TouristGuide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TouristGuideRepository extends JpaRepository<TouristGuide, Long> {
    List<TouristGuide> findByVendorId(Long vendorId);
    List<TouristGuide> findByVendorIdAndIsActive(Long vendorId, Boolean isActive);
    Optional<TouristGuide> findByIdAndVendorId(Long id, Long vendorId);
}