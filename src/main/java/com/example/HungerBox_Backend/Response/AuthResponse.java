package com.example.HungerBox_Backend.Response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Response class used to send authentication details after login.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    /**
     * JWT token for authenticated user.
     */
    private String jwt;

    /**
     * Message related to the authentication process.
     */
    private String message;

    /**
     * Role of the authenticated user.
     * 0 for Customer
     * 1 for Vendor
     */
    private int role; // Integer role (0 for Customer, 1 for Vendor)
}
