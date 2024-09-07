package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Food;
import com.example.HungerBox_Backend.Model.Order;
import com.example.HungerBox_Backend.Model.OrderItem;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Repository.FoodRepository;
import com.example.HungerBox_Backend.Repository.OrderItemRepository;
import com.example.HungerBox_Backend.Repository.OrderRepository;
import com.example.HungerBox_Backend.Request.CreateFoodRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodServiceImplementation implements FoodService {

    @Autowired
    private FoodRepository foodRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private OrderRepository orderRepository;

    /**
     * Creates a new food item for a given vendor based on the provided request.
     *
     * @param request The request containing details about the food item
     * @param vendor The vendor to associate the food item with
     * @return The saved food item
     */
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

        // Save the food item in the database
        Food savedFood = foodRepository.save(food);
        vendor.getFoods().add(savedFood); // Associate the food item with the vendor

        return savedFood;
    }

    /**
     * Deletes a food item by its ID. It also handles removing associated order items
     * and updating the corresponding orders to reflect the deletion.
     *
     * @param foodId The ID of the food item to delete
     * @throws Exception If the food item or associated data is not found
     */
    @Transactional
    public void deleteFood(Long foodId) throws Exception {
        // Find the food item by ID
        Food food = foodRepository.findById(foodId)
                .orElseThrow(() -> new Exception("Food item not found"));

        // Find all order items associated with this food
        List<OrderItem> orderItems = orderItemRepository.findByFood(food);

        // For each order item, update the associated order
        for (OrderItem orderItem : orderItems) {
            Order order = orderRepository.findByItemsContaining(orderItem);
            if (order != null) {
                order.getItems().remove(orderItem); // Remove order item from the order

                // Update order's total price and item count
                order.setTotalItem(order.getItems().size());
                order.setTotalPrice(order.getItems().stream()
                        .mapToLong(OrderItem::getTotalPrice)
                        .sum());

                // Save the updated order
                orderRepository.save(order);
            }

            // Delete the order item
            orderItemRepository.delete(orderItem);
        }

        // Finally, delete the food item from the database
        foodRepository.deleteById(foodId);
    }

    /**
     * Retrieves all food items for a specific vendor and filters by vegetarian or non-vegetarian status.
     *
     * @param vendorId The ID of the vendor whose food items to retrieve
     * @param isVeg Whether to filter by vegetarian food
     * @param isNonVeg Whether to filter by non-vegetarian food
     * @return A list of food items matching the filters
     */
    @Override
    public List<Food> getVendorFood(long vendorId, boolean isVeg, boolean isNonVeg) {
        List<Food> foods = foodRepository.findByVendorVendorId(vendorId);

        // Return all foods if both vegetarian and non-vegetarian options are requested
        if (isVeg && isNonVeg) {
            return foods;
        }

        // Filter based on vegetarian or non-vegetarian preferences
        if (isVeg) {
            foods = filterByVegetarian(foods, isVeg);
        }
        if (isNonVeg) {
            foods = filterByNonVegetarian(foods, isNonVeg);
        }

        return foods;
    }

    // Filters the food list to include only non-vegetarian food items
    private List<Food> filterByNonVegetarian(List<Food> foods, boolean isNonVeg) {
        return foods.stream().filter(food -> !food.isVeg()).collect(Collectors.toList());
    }

    // Filters the food list to include only vegetarian food items
    private List<Food> filterByVegetarian(List<Food> foods, boolean isVeg) {
        return foods.stream().filter(food -> food.isVeg() == isVeg).collect(Collectors.toList());
    }

    /**
     * Searches food items by a keyword (e.g., food name or description).
     *
     * @param keyword The keyword to search for
     * @return A list of matching food items
     */
    @Override
    public List<Food> searchFood(String keyword) {
        return foodRepository.searchFood(keyword);
    }

    /**
     * Finds a food item by its ID.
     *
     * @param foodId The ID of the food item to find
     * @return The found food item
     * @throws Exception If the food item is not found
     */
    @Override
    public Food findFoodById(long foodId) throws Exception {
        Optional<Food> optionalFood = foodRepository.findById(foodId);
        if (optionalFood.isEmpty()) {
            throw new Exception("Food doesn't exist !!!");
        }
        return optionalFood.get();
    }

    /**
     * Updates multiple details of an existing food item.
     *
     * @param id          the ID of the food item to update.
     * @param name        the new name for the food item (if not null or empty).
     * @param price       the new price for the food item (if not null).
     * @param description the new description for the food item (if not null or empty).
     */
    public void updateFoodDetails(Long id, String name, Integer price, String description) {
        // Retrieve the food item by its ID
        Food food = foodRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid food ID"));

        // Update food details if new values are provided
        if (name != null && !name.isEmpty()) {
            food.setFoodName(name);
        }

        if (price != null) {
            food.setPrice(price);
        }

        if (description != null && !description.isEmpty()) {
            food.setDescription(description);
        }

        // Save the updated food item back to the database
        foodRepository.save(food);
    }
}
