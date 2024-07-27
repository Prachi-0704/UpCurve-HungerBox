package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

    List<Food> findByVendorVendorId(long vendorId);

    @Query("SELECT f FROM Food f WHERE f.foodName LIKE %:keyword%")
    List<Food> searchFood(@Param("keyword") String keyword);
}
