package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Food;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Repository.FoodRepository;
import com.example.HungerBox_Backend.Request.CreateFoodRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImplementation implements FoodService{

    @Autowired
    private FoodRepository foodRepository;

    @Override
    public Food createFood(CreateFoodRequest request, Vendor vendor) {

        Food food = new Food();

        food.setVendor(vendor);

        food.setDescription(request.getDescription());

        food.setImages(request.getImages());

        food.setFoodName(request.getFoodName());

        food.setPrice(request.getPrice());

        food.setVeg(request.isVeg());

        food.setNonVeg(request.isNonVeg());

        food.setCalories(request.getCalories());

        Food savedFood = foodRepository.save(food);

        vendor.getFoods().add(savedFood);

        return savedFood;
    }

    @Override
    public void deleteFood(Long foodId) throws Exception {

        Food food = findFoodById(foodId);

        food.setVendor(null);

        foodRepository.delete(food);
    }

    @Override
    public List<Food> getVendorFood(long vendorId, boolean isVeg, boolean isNonVeg) {

        List<Food> foods = foodRepository.findByVendorVendorId(vendorId);

        if(isVeg && isNonVeg){
            return foods;
        }

        if(isVeg){
            foods = filterByVegetarian(foods, isVeg);
        }

        if(isNonVeg){
            foods = filterByNonVegetarian(foods, isNonVeg);
        }

        return foods;
    }

    private List<Food> filterByNonVegetarian(List<Food> foods, boolean isNonVeg) {
        return foods.stream().filter(food -> food.isVeg()==false).collect(Collectors.toList());
    }

    private List<Food> filterByVegetarian(List<Food> foods, boolean isVeg) {
        return foods.stream().filter(food -> food.isVeg()==isVeg).collect(Collectors.toList());
    }

    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    @Override
    public Food findFoodById(long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(foodId);

        if(optionalFood.isEmpty()){
            throw new Exception("Food doesn't exist !!!");
        }

        return optionalFood.get();
    }

    @Override
    public Food updateAvailabilityStatus(long foodId) throws Exception {

        Food food = findFoodById(foodId);

        food.setAvailable(!food.isAvailable());

        return foodRepository.save(food);
    }

}