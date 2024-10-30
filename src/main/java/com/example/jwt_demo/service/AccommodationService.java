package com.example.jwt_demo.service;
import com.example.jwt_demo.exceptions.ResourceNotFoundException;
import com.example.jwt_demo.model.Accommodation;
import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.repository.vendor.AccommodationRepository;
import com.example.jwt_demo.repository.vendor.VendorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final VendorRepository vendorRepository;

    public Accommodation create(Accommodation accommodation, Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        accommodation.setVendor(vendor);
        return accommodationRepository.save(accommodation);
    }

    public Accommodation update(Long id, Accommodation accommodationDetails, Long vendorId) {
        Accommodation accommodation = accommodationRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found"));

        BeanUtils.copyProperties(accommodationDetails, accommodation, "id", "vendor", "createdAt");
        return accommodationRepository.save(accommodation);
    }

    public void delete(Long id, Long vendorId) {
        Accommodation accommodation = accommodationRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found"));
        accommodationRepository.delete(accommodation);
    }

    public List<Accommodation> getAllByVendor(Long vendorId) {
        return accommodationRepository.findByVendorId(vendorId);
    }

    public Accommodation getOne(Long id, Long vendorId) {
        return accommodationRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Accommodation not found"));
    }

    public List<Accommodation> getAvailableAccommodations(Long vendorId) {
        return accommodationRepository.findByVendorIdAndIsAvailable(vendorId, true);
    }
}
