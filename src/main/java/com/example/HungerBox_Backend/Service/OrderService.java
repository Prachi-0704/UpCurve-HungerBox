package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Order;

import java.util.List;

public interface OrderService {

    public Order updateOrderStatus(long orderId, String orderStatus) throws Exception;

    public List<Order> getVendorsOrder(long vendorId, String orderStatus) throws Exception;

    public Order findOrderByOrderId(long orderId) throws Exception;
}
