package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Order;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Request.OrderRequest;

import java.util.List;

public interface OrderService {
    public Order createOrder(OrderRequest request, User user) throws Exception;

    public Order updateOrderStatus(long orderId, String orderStatus) throws Exception;

    public void cancelOrder(long orderId) throws Exception;

    public List<Order> getUsersOrder(long userId) throws Exception;

    public List<Order> getVendorsOrder(long vendorId, String orderStatus) throws Exception;

    public Order findOrderByOrderId(long orderId) throws Exception;
}
