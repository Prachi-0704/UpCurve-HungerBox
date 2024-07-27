package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Security.JwtProvider;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserByJwtToken(String jwt) throws Exception {

        String emailId = jwtProvider.getEmailIdFromJwtToken(jwt);

        User user = findUserByEmailId(emailId);

        return user;
    }

    @Override
    public User findUserByEmailId(String emailId) throws Exception {

        User user = userRepository.findByEmailId(emailId);

        if(user == null){
            throw new BadCredentialsException("User not found !!!");
        }

        return user;
    }
}
