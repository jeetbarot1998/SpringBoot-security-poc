package com.example.jwt_demo.model.dtos;

import lombok.Data;

import java.util.List;

@Data
public class FoodItemDTO {
    private String name;
    private String description;
    private Double price;
    private String category;
    private Boolean isAvailable;
    private String imageUrl;
    private Integer preparationTime;
    private List<String> ingredients;
}