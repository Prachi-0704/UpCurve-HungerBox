package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.Food;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Request.CreateFoodRequest;
import com.example.HungerBox_Backend.Response.MessageResponse;
import com.example.HungerBox_Backend.Service.FoodService;
import com.example.HungerBox_Backend.Service.UserService;
import com.example.HungerBox_Backend.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/food")
public class AdminFoodController {
    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    /**TESTED**/
    @PostMapping
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest request,
                                           @RequestHeader("Authorization") String authHeader) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        Vendor vendor = vendorService.findVendorById(request.getVendorId());

        Food food = foodService.createFood(request, vendor);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    /**TESTED**/
    @DeleteMapping("/delete/{foodId}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable long foodId,
                                                      @RequestHeader("Authorization") String authHeader) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        foodService.deleteFood(foodId);

        MessageResponse messageResponse = new MessageResponse();

        messageResponse.setMessage("Food deleted successfully");

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    /**TESTED**/
    /**updates whether the food is available or not**/
    @PutMapping("/{foodId}/status")
    public ResponseEntity<Food> updateFoodAvailibility(@PathVariable long foodId,
                                           @RequestHeader("Authorization") String authHeader) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        Food food = foodService.updateAvailabilityStatus(foodId);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }
}
