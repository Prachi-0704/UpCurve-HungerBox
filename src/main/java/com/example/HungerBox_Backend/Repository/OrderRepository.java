package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    //find order by customerId
    public List<Order> findByCustomerUserId(long userId);

    //find order by vendor id
    public List<Order> findByVendorVendorId(long vendorId);
}
