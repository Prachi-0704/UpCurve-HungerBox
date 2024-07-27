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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImplementation implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private CartService cartService;

    @Override
    public Order createOrder(OrderRequest request, User user) throws Exception{

        Vendor vendor = vendorService.findVendorById(request.getVendorId());

        Order newOrder = new Order();

        newOrder.setCustomer(user);

        newOrder.setCreatedAt(LocalDateTime.now());

        newOrder.setOrderStatus("PENDING");

        newOrder.setVendor(vendor);

        Cart cart = cartService.findCartByUserId(user.getUserId());

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem cartItem : cart.getItems()){

            OrderItem orderItem = new OrderItem();

            orderItem.setFood(cartItem.getFood());

            orderItem.setQuantity(cartItem.getQuantity());

            orderItem.setTotalPrice(cartItem.getTotalPrice());

            OrderItem savedOrderItem = orderItemRepository.save(orderItem);

            orderItems.add(savedOrderItem);
        }

        newOrder.setItems(orderItems);

        long totalPrice = cartService.calculateCartTotal(cart);

        newOrder.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(newOrder);

        vendor.getVendorOrders().add(savedOrder);

        return savedOrder;
    }

    @Override
    public Order updateOrderStatus(long orderId, String orderStatus) throws Exception {

        Order order = findOrderByOrderId(orderId);

        if(orderStatus.equals("COMPLETED") || orderStatus.equals("PENDING") || orderStatus.equals("TAKEN"))
        {
            order.setOrderStatus(orderStatus);
            return order;
        }

        throw new Exception("Please select a valid order status");
    }

    @Override
    public void cancelOrder(long orderId) throws Exception {

        Order order = findOrderByOrderId(orderId);

        orderRepository.delete(order);
    }

    @Override
    public List<Order> getUsersOrder(long userId) throws Exception {
        return orderRepository.findByCustomerUserId(userId);
    }

    @Override
    public List<Order> getVendorsOrder(long vendorId, String orderStatus) throws Exception {

        List<Order> orders = orderRepository.findByVendorVendorId(vendorId);

        if(orderStatus!=null){
            orders = orders.stream().filter(order ->
                    order.getOrderStatus().equals(orderStatus)).collect(Collectors.toList());
        }

        return orders;
    }

    @Override
    public Order findOrderByOrderId(long orderId) throws Exception {

        Optional<Order> optionalOrder = orderRepository.findById(orderId);

        if(optionalOrder.isEmpty()){
            throw new Exception("Order not found");
        }

        return optionalOrder.get();
    }

}
