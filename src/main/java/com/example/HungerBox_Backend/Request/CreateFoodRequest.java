package com.example.HungerBox_Backend.Request;

import lombok.Data;

import java.util.List;

@Data
public class CreateFoodRequest {

    private String foodName;

    private String description;

    private long price;

    private List<String> images;

    private long vendorId;

    private boolean isVeg;

    private long totalAvailability;

    private boolean isNonVeg;

    private long calories;
}
