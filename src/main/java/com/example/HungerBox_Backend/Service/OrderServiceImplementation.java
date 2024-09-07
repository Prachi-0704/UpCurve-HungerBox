package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.*;
import com.example.HungerBox_Backend.Repository.OrderItemRepository;
import com.example.HungerBox_Backend.Repository.OrderRepository;
import com.example.HungerBox_Backend.Request.OrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService{

    // Injecting required repositories and services
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private CartService cartService;

    /**
     * Updates the status of an order based on the provided order ID.
     * Valid statuses include "COMPLETED", "PENDING", and "TAKEN".
     *
     * @param orderId The ID of the order to update
     * @param orderStatus The new status to set for the order
     * @return The updated order
     * @throws Exception If the order is not found or the status is invalid
     */
    @Override
    public Order updateOrderStatus(long orderId, String orderStatus) throws Exception {

        // Find the order by its ID
        Order order = findOrderByOrderId(orderId);

        // Update the order status if it is valid
        if(orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING") || orderStatus.equals("TAKEN")) {
            order.setOrderStatus(orderStatus);
            orderRepository.save(order);
            return order;
        }

        // Throw an exception if the provided status is invalid
        throw new Exception("Please select a valid order status");
    }

    /**
     * Retrieves all orders placed with a specific vendor.
     * Optionally filters orders by their status.
     *
     * @param vendorId The ID of the vendor
     * @param orderStatus The status to filter orders by (optional)
     * @return A list of orders matching the vendor and status criteria
     * @throws Exception If an error occurs during retrieval
     */
    @Override
    public List<Order> getVendorsOrder(long vendorId, String orderStatus) throws Exception {

        // Fetch the list of orders placed with the vendor
        List<Order> orders = orderRepository.findOrdersByVendorId(vendorId);
        System.out.println(orders);

        // If no status is provided, return all orders
        if (orderStatus == null || orderStatus.isEmpty()) {
            return orders;
        } else {
            // Otherwise, filter the orders by the provided status
            orders = orders.stream()
                    .filter(order -> order.getOrderStatus() != null && order.getOrderStatus().equals(orderStatus))
                    .collect(Collectors.toList());
        }

        return orders;
    }

    /**
     * Finds and returns an order based on its ID.
     *
     * @param orderId The ID of the order to retrieve
     * @return The retrieved order
     * @throws Exception If the order is not found
     */
    @Override
    public Order findOrderByOrderId(long orderId) throws Exception {

        // Retrieve the order from the repository
        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        // Return the found order or throw an exception if not found
        return optionalOrder.get();
    }
}
