package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Food;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Request.CreateFoodRequest;

import java.util.List;

public interface FoodService {
    public Food createFood(CreateFoodRequest request, Vendor vendor);

    void deleteFood(Long foodId) throws Exception;

    public List<Food> getVendorFood(long vendorId, boolean isVeg, boolean isNonVeg);

    public List<Food> searchFood(String keyword);

    public Food findFoodById(long foodId) throws Exception;

    public Food updateAvailabilityStatus(long foodId) throws Exception;
}
