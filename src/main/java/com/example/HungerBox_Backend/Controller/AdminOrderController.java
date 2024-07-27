package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.Order;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Service.OrderService;
import com.example.HungerBox_Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @GetMapping("/order/vendor/{vendorId}")
    public ResponseEntity<List<Order>> getOrderHistory(
            @PathVariable long vendorId,
            @RequestParam(required = false) String orderStatus,
            @RequestHeader("Authorization") String authHeader
    )throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getVendorsOrder(vendorId, orderStatus);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/order/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable long vendorId,
            @RequestParam(required = false) String orderStatus,
            @RequestHeader("Authorization") String authHeader
    )throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        User user = userService.findUserByJwtToken(jwt);

        Order orders = orderService.updateOrderStatus(vendorId, orderStatus);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }


}
