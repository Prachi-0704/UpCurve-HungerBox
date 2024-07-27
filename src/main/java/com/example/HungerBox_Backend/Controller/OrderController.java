package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.CartItem;
import com.example.HungerBox_Backend.Model.Order;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Request.AddCartItemRequest;
import com.example.HungerBox_Backend.Request.OrderRequest;
import com.example.HungerBox_Backend.Service.CartService;
import com.example.HungerBox_Backend.Service.OrderService;
import com.example.HungerBox_Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    @PutMapping("/order")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request,
                                                @RequestHeader("Authorization") String authHeader
    )throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        User user = userService.findUserByJwtToken(jwt);

        Order order = orderService.createOrder(request, user);

        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    @GetMapping("/order")
    public ResponseEntity<List<Order>> getOrderHistory(
                                             @RequestHeader("Authorization") String authHeader
    )throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        User user = userService.findUserByJwtToken(jwt);

        List<Order> orders = orderService.getUsersOrder(user.getUserId());

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
