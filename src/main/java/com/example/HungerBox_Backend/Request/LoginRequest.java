package com.example.HungerBox_Backend.Request;

import lombok.Data;

@Data
public class LoginRequest {

    private String emailId;

    private String password;
}
