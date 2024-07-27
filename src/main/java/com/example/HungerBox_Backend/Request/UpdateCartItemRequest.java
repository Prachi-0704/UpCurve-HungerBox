package com.example.HungerBox_Backend.Request;

import lombok.Data;

@Data
public class UpdateCartItemRequest {

    private long cartItemId;

    private int quantity;
}
