package com.example.jwt_demo.repository.vendor;
import com.example.jwt_demo.model.FoodItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {
    List<FoodItem> findByVendorId(Long vendorId);
    List<FoodItem> findByVendorIdAndIsAvailable(Long vendorId, Boolean isAvailable);
    Optional<FoodItem> findByIdAndVendorId(Long id, Long vendorId);
}
