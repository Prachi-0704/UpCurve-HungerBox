package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.User;

public interface UserService {
    public User findUserByJwtToken(String jwt) throws Exception;

    public User findUserByEmailId(String emailId) throws Exception;
}
