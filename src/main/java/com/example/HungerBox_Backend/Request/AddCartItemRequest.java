package com.example.HungerBox_Backend.Request;

import lombok.Data;

import java.util.List;

@Data
public class AddCartItemRequest {

    private long foodId;

    private int quantity;
}
