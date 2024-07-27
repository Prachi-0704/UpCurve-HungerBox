package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Model.Vendor;
import com.example.HungerBox_Backend.Request.CreateVendorRequest;
import com.example.HungerBox_Backend.Response.MessageResponse;
import com.example.HungerBox_Backend.Service.UserService;
import com.example.HungerBox_Backend.Service.VendorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** VENDOR CONTROLLER FOR VENDOR OWNER **/

@RestController
@RequestMapping("/api/admin/vendors")
public class AdminVendorController {

    @Autowired
    private VendorService vendorService;

    @Autowired
    private UserService userService;

    /**TESTED**/
    @PostMapping()
    public ResponseEntity<Vendor> createVendor(
            @RequestBody CreateVendorRequest req,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        Vendor vendor = vendorService.createVendor(req, user);

        return new ResponseEntity<>(vendor, HttpStatus.CREATED);
    }

    /**TESTED**/
    @PutMapping("/{vendorId}/update")
    public ResponseEntity<Vendor> updateVendor(
            @RequestBody CreateVendorRequest req,
            @RequestHeader("Authorization") String authHeader,
            @PathVariable long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        Vendor vendor = vendorService.updateVendor(vendorId, req);

        return new ResponseEntity<>(vendor, HttpStatus.CREATED);
    }

    /**TESTED**/
    @DeleteMapping("/{vendorId}/delete")
    public ResponseEntity<MessageResponse> deleteVendor(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        vendorService.deleteVendor(vendorId);

        MessageResponse response = new MessageResponse();

        response.setMessage("Vendor Deleted Successfully!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**TESTED**/
    @PutMapping("/{vendorId}/status")
    public ResponseEntity<Vendor> updateVendorStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        Vendor vendor = vendorService.updateVendorStatus(vendorId);

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    /**TESTED**/
    @GetMapping("/user")
    public ResponseEntity<Vendor> findVendorByUserId(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User owner = userService.findUserByJwtToken(jwt);

        Vendor vendor = vendorService.getVendorByUserId(owner.getUserId());

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }
}
