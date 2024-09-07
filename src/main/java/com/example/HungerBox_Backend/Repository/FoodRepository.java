package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling CRUD operations related to Food entities.
 */
@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    /**
     * Finds all food items associated with a specific vendor.
     *
     * @param vendorId the unique identifier of the vendor
     * @return a list of Food items associated with the specified vendor
     */
    List<Food> findByVendorVendorId(long vendorId);

    /**
     * Searches for food items based on a keyword in the food name.
     *
     * @param keyword the keyword to search for in food names
     * @return a list of Food items that match the search criteria
     */
    @Query("SELECT f FROM Food f WHERE f.foodName LIKE %:keyword%")
    List<Food> searchFood(@Param("keyword") String keyword);
}







