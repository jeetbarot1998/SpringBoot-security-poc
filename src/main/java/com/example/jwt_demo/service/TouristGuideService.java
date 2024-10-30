package com.example.jwt_demo.service;

import com.example.jwt_demo.model.TouristGuide;
import com.example.jwt_demo.repository.vendor.TouristGuideRepository;
import com.example.jwt_demo.exceptions.ResourceNotFoundException;
import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.repository.vendor.VendorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class TouristGuideService {
    private final TouristGuideRepository touristSpotRepository;
    private final VendorRepository vendorRepository;

    public TouristGuide create(TouristGuide touristSpot, Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        touristSpot.setVendor(vendor);
        return touristSpotRepository.save(touristSpot);
    }

    public TouristGuide update(Long id, TouristGuide touristSpotDetails, Long vendorId) {
        TouristGuide touristSpot = touristSpotRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist spot not found"));

        BeanUtils.copyProperties(touristSpotDetails, touristSpot, "id", "vendor", "createdAt");
        return touristSpotRepository.save(touristSpot);
    }

    public void delete(Long id, Long vendorId) {
        TouristGuide touristSpot = touristSpotRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist spot not found"));
        touristSpotRepository.delete(touristSpot);
    }

    public List<TouristGuide> getAllByVendor(Long vendorId) {
        return touristSpotRepository.findByVendorId(vendorId);
    }

    public TouristGuide getOne(Long id, Long vendorId) {
        return touristSpotRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Tourist spot not found"));
    }

    public List<TouristGuide> getActiveSpots(Long vendorId) {
        return touristSpotRepository.findByVendorIdAndIsActive(vendorId, true);
    }
}