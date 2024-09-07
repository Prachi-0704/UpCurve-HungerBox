package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerOrderRepository extends JpaRepository<CustomerOrder, Long> {

        List<CustomerOrder> findByCustomerUserId(long userId);
}
