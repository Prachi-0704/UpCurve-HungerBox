package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomerUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor-based dependency injection for UserRepository
    @Autowired
    public CustomerUserDetailService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads user details by username (in this case, the email ID).
     *
     * @param username The username (email) provided during login.
     * @return UserDetails object containing the user credentials and roles.
     * @throws UsernameNotFoundException if the user is not found in the repository.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Find the user by email ID (username)
        User user = userRepository.findByEmailId(username);

        // If user is not found, throw an exception
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email " + username);
        }

        // Create a list to hold the user's granted authorities (roles)
        List<GrantedAuthority> authorities = new ArrayList<>();

        // Add authority based on the user's role (0 for customer, 1 for vendor)
        if (user.getRole() == 1) {
            authorities.add(new SimpleGrantedAuthority("ROLE_VENDOR"));
        } else {
            authorities.add(new SimpleGrantedAuthority("ROLE_CUSTOMER"));
        }

        // Return a UserDetails object with the user's email, password, and authorities
        return new org.springframework.security.core.userdetails.User(user.getEmailId(), user.getPassword(), authorities);
    }
}
