package com.example.HungerBox_Backend.Request;

import lombok.Data;

/**
 * Request class used to update a cart item.
 */
@Data
public class UpdateCartItemRequest {

    /**
     * Unique identifier for the cart item to be updated.
     */
    private long cartItemId;

    /**
     * The new quantity for the cart item.
     */
    private int quantity;
}
