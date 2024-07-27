package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}
