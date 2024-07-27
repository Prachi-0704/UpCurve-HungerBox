package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.DTO.VendorDTO;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Request.CreateVendorRequest;
import com.example.HungerBox_Backend.Service.UserService;
import com.example.HungerBox_Backend.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** VENDOR CONTROLLER FOR CUSTOMERS **/

@RestController
@RequestMapping("/api/vendors")
public class CustomerVendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private UserService userService;

    /**TESTED**/
    @GetMapping("/search")
    public ResponseEntity<List<Vendor>> searchVendor(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String keyword
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        List<Vendor> vendor = vendorService.searchVendor(keyword);

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    /**TESTED**/
    @GetMapping()
    public ResponseEntity<List<Vendor>> getAllVendor(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        List<Vendor> vendor = vendorService.getAllVendor();

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    /**TESTED**/
    @GetMapping("/{vendorId}")
    public ResponseEntity<Vendor> findVendorById(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        Vendor vendor = vendorService.findVendorById(vendorId);

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }
}
