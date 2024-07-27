package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.Food;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Service.FoodService;
import com.example.HungerBox_Backend.Service.UserService;
import com.example.HungerBox_Backend.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/food")
public class CustomerFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    /**TESTED**/
    @GetMapping("/search")
    public ResponseEntity<List<Food>> searchFood(@RequestParam String keyword,
                                           @RequestHeader("Authorization") String authHeader) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        List<Food> foods = foodService.searchFood(keyword);

        return new ResponseEntity<>(foods, HttpStatus.OK);
    }


    /**TESTED**/

    /** Get food from a particular vendor. **/
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<List<Food>> getVendorFood(
            @RequestParam boolean veg,
            @RequestParam boolean nonVeg,
            @PathVariable long vendorId,
            @RequestHeader("Authorization") String authHeader) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        List<Food> foods = foodService.getVendorFood(vendorId, veg, nonVeg);

        return new ResponseEntity<>(foods, HttpStatus.OK);
    }
}
