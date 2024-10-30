package com.example.jwt_demo.service;

import com.example.jwt_demo.exceptions.ResourceNotFoundException;
import com.example.jwt_demo.model.FoodItem;
import com.example.jwt_demo.model.Vendor;
import com.example.jwt_demo.repository.vendor.FoodItemRepository;
import com.example.jwt_demo.repository.vendor.VendorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodItemService {
    private final FoodItemRepository foodItemRepository;
    private final VendorRepository vendorRepository;

    public FoodItem create(FoodItem foodItem, Long vendorId) {
        Vendor vendor = vendorRepository.findById(vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor not found"));
        foodItem.setVendor(vendor);
        return foodItemRepository.save(foodItem);
    }

    public FoodItem update(Long id, FoodItem foodItemDetails, Long vendorId) {
        FoodItem foodItem = foodItemRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found"));

        BeanUtils.copyProperties(foodItemDetails, foodItem, "id", "vendor", "createdAt");
        return foodItemRepository.save(foodItem);
    }

    public void delete(Long id, Long vendorId) {
        FoodItem foodItem = foodItemRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found"));
        foodItemRepository.delete(foodItem);
    }

    public List<FoodItem> getAllByVendor(Long vendorId) {
        return foodItemRepository.findByVendorId(vendorId);
    }

    public FoodItem getOne(Long id, Long vendorId) {
        return foodItemRepository.findByIdAndVendorId(id, vendorId)
                .orElseThrow(() -> new ResourceNotFoundException("Food item not found"));
    }
}
