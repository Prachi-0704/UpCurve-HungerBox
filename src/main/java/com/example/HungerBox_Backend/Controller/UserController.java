package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**TESTED**/
    @GetMapping("/profile")
    public ResponseEntity<?> findUserByJwtToken(@RequestHeader("Authorization") String authHeader) {

        try {
            // Extract the JWT token from the Authorization header
            String jwt = authHeader.replace("Bearer ", "");

            // Find the user by JWT token
            User user = userService.findUserByJwtToken(jwt);

            // Return the user details with an OK status
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {

            // Return an appropriate error response with the exception message
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}