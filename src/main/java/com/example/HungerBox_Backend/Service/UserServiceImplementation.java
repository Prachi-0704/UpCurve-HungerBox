package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Security.JwtProvider;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    /**
     * Retrieves a user based on the provided JWT token.
     *
     * @param jwt the JWT token containing the user's email.
     * @return the User associated with the email in the JWT.
     * @throws Exception if the user cannot be found.
     */
    @Override
    public User findUserByJwtToken(String jwt) throws Exception {
        // Extract the email ID from the JWT token
        String emailId = jwtProvider.getEmailIdFromJwtToken(jwt);

        // Find the user by email ID
        User user = findUserByEmailId(emailId);

        return user;
    }

    /**
     * Finds a user by their email ID.
     *
     * @param emailId the email ID of the user to find.
     * @return the User with the specified email ID.
     * @throws BadCredentialsException if no user is found with the given email ID.
     */
    @Override
    public User findUserByEmailId(String emailId) throws Exception {
        // Find the user in the repository by email ID
        User user = userRepository.findByEmailId(emailId);

        // Throw an exception if the user is not found
        if (user == null) {
            throw new BadCredentialsException("User not found !!!");
        }

        return user;
    }
}
