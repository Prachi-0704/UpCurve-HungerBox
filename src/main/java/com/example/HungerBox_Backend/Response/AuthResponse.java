package com.example.HungerBox_Backend.Response;

import com.example.HungerBox_Backend.Model.USER_ROLE;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String jwt;

    private String message;

    private USER_ROLE role;
}