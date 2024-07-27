package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByCustomerUserId(long userId);
}
