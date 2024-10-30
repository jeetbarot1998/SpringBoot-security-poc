package com.example.jwt_demo.model.mapper;

import com.example.jwt_demo.model.FoodItem;
import com.example.jwt_demo.model.dtos.FoodItemDTO;
import org.springframework.stereotype.Component;

@Component
public class FoodItemMapper {
    public FoodItemDTO toDTO(FoodItem entity) {
        FoodItemDTO dto = new FoodItemDTO();
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setPrice(entity.getPrice());
        dto.setCategory(entity.getCategory());
        dto.setIsAvailable(entity.getIsAvailable());
        dto.setImageUrl(entity.getImageUrl());
        dto.setPreparationTime(entity.getPreparationTime());
        dto.setIngredients(entity.getIngredients());
        return dto;
    }
}