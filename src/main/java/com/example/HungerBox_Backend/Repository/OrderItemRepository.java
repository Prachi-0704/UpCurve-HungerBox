package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Food;
import com.example.HungerBox_Backend.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repository interface for handling CRUD operations related to OrderItem entities.
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    /**
     * Finds all OrderItems associated with a specific Food item.
     *
     * @param food the Food item for which to find associated OrderItems
     * @return a list of OrderItem entities that are associated with the specified Food item
     */
    @Query("SELECT oi FROM OrderItem oi WHERE oi.food = :food")
    List<OrderItem> findByFood(@Param("food") Food food);
}