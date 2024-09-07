//
//
//package com.example.HungerBox_Backend.Controller;
//
//import com.example.HungerBox_Backend.Model.Wallet;
//import com.example.HungerBox_Backend.Repository.WalletRepository;
//import com.example.HungerBox_Backend.Security.JwtProvider;
//import com.example.HungerBox_Backend.Model.Cart;
//import com.example.HungerBox_Backend.Model.User;
//import com.example.HungerBox_Backend.Repository.CartRepository;
//import com.example.HungerBox_Backend.Repository.UserRepository;
//import com.example.HungerBox_Backend.Request.LoginRequest;
//import com.example.HungerBox_Backend.Response.AuthResponse;
//import com.example.HungerBox_Backend.Service.CustomerUserDetailService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.time.LocalDate;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//@RestController
//@RequestMapping("/auth")
//public class AuthController {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private PasswordEncoder passwordEncoder;
//
//    @Autowired
//    private JwtProvider jwtProvider;
//
//    @Autowired
//    private CustomerUserDetailService customerUserDetailService;
//
//    @Autowired
//    private CartRepository cartRepository;
//
//    @PostMapping("/register")
//    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
//        // Check if email already exists
//        User isEmailIdExist = userRepository.findByEmailId(user.getEmailId());
//
//        if (isEmailIdExist != null) {
//            throw new Exception("Email already exists!");
//        }
//
//        // Create and save the new user
//        User createdUser = new User();
//        createdUser.setEmailId(user.getEmailId());
//        createdUser.setFullName(user.getFullName());
//        createdUser.setRole(user.getRole()); // Role is an integer (0 or 1)
//        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));
//
//        User savedUser = userRepository.save(createdUser);
//
//        // Create and save the cart for the new user
//        Cart cart = new Cart();
//        cart.setCustomer(savedUser);
//        cartRepository.save(cart);
//
//        // Create a list of authorities based on the user's role
//        List<GrantedAuthority> authorities = Collections.singletonList(
//                new SimpleGrantedAuthority(savedUser.getRole() == 1 ? "ROLE_VENDOR" : "ROLE_CUSTOMER")
//        );
//
//        // Create an Authentication object
//        Authentication authentication = new UsernamePasswordAuthenticationToken(
//                savedUser.getEmailId(),
//                null,  // Password is not required for authentication token here
//                authorities  // List of authorities
//        );
//
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//
//        // Generate JWT token
//        String jwt = jwtProvider.generateToken(authentication);
//
//        // Prepare and return the response
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setMessage("Registration Successful!");
//        authResponse.setRole(savedUser.getRole()); // Integer role (0 or 1)
//        authResponse.setJwt(jwt); // Add token to the response
//
//        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
//    }
//
//    /**COMMENTED TO TRY REFRESH WALLET FUNCTIONALITY**/
//  /**  @PostMapping("/login")
//    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {
//        String userEmailId = loginRequest.getEmailId();
//        String userPassword = loginRequest.getPassword();
//
//        Authentication authentication = authenticate(userEmailId, userPassword);
//
//        // Generate JWT token
//        String jwt = jwtProvider.generateToken(authentication);
//
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//        // Extract role as integer
//        int role = authorities.isEmpty() ? 0 : authorities.iterator().next().getAuthority().equals("ROLE_VENDOR") ? 1 : 0;
//
//        AuthResponse authResponse = new AuthResponse();
//        authResponse.setMessage("Login Successful!");
//        authResponse.setRole(role); // Integer role (0 or 1)
//        authResponse.setJwt(jwt);
//
//        return new ResponseEntity<>(authResponse, HttpStatus.OK);
//    }**/
//
//  /**TRYING REFRESH WALLET ON NEW DAY**/
//
//  @PostMapping("/login")
//  public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {
//      String userEmailId = loginRequest.getEmailId();
//      String userPassword = loginRequest.getPassword();
//
//      Authentication authentication = authenticate(userEmailId, userPassword);
//
//      // Generate JWT token
//      String jwt = jwtProvider.generateToken(authentication);
//
//      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//
//      // Extract role as integer
//      int role = authorities.isEmpty() ? 0 : authorities.iterator().next().getAuthority().equals("ROLE_VENDOR") ? 1 : 0;
//
//      User user = userRepository.findByEmailId(userEmailId);
//
//      if (user != null) {
//          LocalDate currentDate = LocalDate.now();
//
//          // If it's the first login or if the current date is different from the last login date
//          if (user.getPreviousLoginDate() == null || !currentDate.equals(user.getPreviousLoginDate())) {
//              refreshWallet(user);
//          }
//
//          // Update the login dates
//          user.setPreviousLoginDate(currentDate);
//          userRepository.save(user);
//      } else {
//          throw new RuntimeException("Invalid credentials");
//      }
//
//      AuthResponse authResponse = new AuthResponse();
//      authResponse.setMessage("Login Successful!");
//      authResponse.setRole(role); // Integer role (0 or 1)
//      authResponse.setJwt(jwt);
//
//      return new ResponseEntity<>(authResponse, HttpStatus.OK);
//  }
//
//    @Autowired
//    WalletRepository walletRepository;
//
//    private void refreshWallet(User user) {
//        // Try to find the wallet associated with the user
//        Wallet wallet = walletRepository.findByUserUserId(user.getUserId());
//
//        // If the wallet is not found, create a new one
//        if (wallet == null) {
//            wallet = new Wallet();
//            wallet.setUser(user); // Associate the wallet with the user
//            wallet.setBalance(2000); // Set the initial balance
//            walletRepository.save(wallet); // Save the new wallet to the repository
//        } else {
//            // If wallet exists, just update the balance
//            wallet.setBalance(2000);
//            walletRepository.save(wallet);
//        }
//    }
//
//
//
//    private Authentication authenticate(String userEmailId, String userPassword) {
//        UserDetails userDetails = customerUserDetailService.loadUserByUsername(userEmailId);
//
//        if (userDetails == null) {
//            throw new BadCredentialsException("Invalid EmailId !!!");
//        }
//
//        if (!passwordEncoder.matches(userPassword, userDetails.getPassword())) {
//            throw new BadCredentialsException("Invalid Password !!!");
//        }
//
//        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//    }
//}






































package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.Wallet;
import com.example.HungerBox_Backend.Repository.WalletRepository;
import com.example.HungerBox_Backend.Security.JwtProvider;
import com.example.HungerBox_Backend.Model.Cart;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Repository.CartRepository;
import com.example.HungerBox_Backend.Repository.UserRepository;
import com.example.HungerBox_Backend.Request.LoginRequest;
import com.example.HungerBox_Backend.Response.AuthResponse;
import com.example.HungerBox_Backend.Service.CustomerUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private CustomerUserDetailService customerUserDetailService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private WalletRepository walletRepository;

    /**
     * Registers a new user.
     *
     * @param user the user details
     * @return response containing registration status and JWT token
     * @throws Exception if the email already exists
     */
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> createUserHandler(@RequestBody User user) throws Exception {
        // Check if email already exists
        if (userRepository.findByEmailId(user.getEmailId()) != null) {
            throw new Exception("Email already exists!");
        }

        // Create and save the new user
        User createdUser = new User();
        createdUser.setEmailId(user.getEmailId());
        createdUser.setFullName(user.getFullName());
        createdUser.setRole(user.getRole()); // Role is an integer (0 or 1)
        createdUser.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(createdUser);

        // Create and save the cart for the new user
        Cart cart = new Cart();
        cart.setCustomer(savedUser);
        cartRepository.save(cart);

        // Create a list of authorities based on the user's role
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(savedUser.getRole() == 1 ? "ROLE_VENDOR" : "ROLE_CUSTOMER")
        );

        // Create an Authentication object
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                savedUser.getEmailId(),
                null,  // Password is not required for authentication token here
                authorities  // List of authorities
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Generate JWT token
        String jwt = jwtProvider.generateToken(authentication);

        // Prepare and return the response
        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Registration Successful!");
        authResponse.setRole(savedUser.getRole()); // Integer role (0 or 1)
        authResponse.setJwt(jwt); // Add token to the response

        return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
    }

    /**
     * Logs in an existing user.
     *
     * @param loginRequest the login request containing email and password
     * @return response containing login status and JWT token
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signIn(@RequestBody LoginRequest loginRequest) {
        String userEmailId = loginRequest.getEmailId();
        String userPassword = loginRequest.getPassword();

        // Authenticate user
        Authentication authentication = authenticate(userEmailId, userPassword);

        // Generate JWT token
        String jwt = jwtProvider.generateToken(authentication);

        // Extract authorities and determine role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        int role = authorities.isEmpty() ? 0 : authorities.iterator().next().getAuthority().equals("ROLE_VENDOR") ? 1 : 0;

        // Find and update user information
        User user = userRepository.findByEmailId(userEmailId);

        if (user != null) {
            LocalDate currentDate = LocalDate.now();

            // If it's the first login or if the current date is different from the last login date
            if (user.getPreviousLoginDate() == null || !currentDate.equals(user.getPreviousLoginDate())) {
                refreshWallet(user);
            }

            // Update the login dates
            user.setPreviousLoginDate(currentDate);
            userRepository.save(user);
        } else {
            throw new RuntimeException("Invalid credentials");
        }

        AuthResponse authResponse = new AuthResponse();
        authResponse.setMessage("Login Successful!");
        authResponse.setRole(role); // Integer role (0 or 1)
        authResponse.setJwt(jwt);

        return new ResponseEntity<>(authResponse, HttpStatus.OK);
    }

    /**
     * Refreshes or creates a wallet for the given user.
     *
     * @param user the user whose wallet needs to be refreshed
     */
    private void refreshWallet(User user) {
        Wallet wallet = walletRepository.findByUserUserId(user.getUserId());

        if (wallet == null) {
            wallet = new Wallet();
            wallet.setUser(user); // Associate the wallet with the user
            wallet.setBalance(2000); // Set the initial balance
        } else {
            wallet.setBalance(2000); // Update the balance
        }

        walletRepository.save(wallet); // Save the wallet to the repository
    }

    /**
     * Authenticates the user using email and password.
     *
     * @param userEmailId the user's email ID
     * @param userPassword the user's password
     * @return the authentication object
     */
    private Authentication authenticate(String userEmailId, String userPassword) {
        UserDetails userDetails = customerUserDetailService.loadUserByUsername(userEmailId);

        if (userDetails == null) {
            throw new BadCredentialsException("Invalid EmailId !!!");
        }

        if (!passwordEncoder.matches(userPassword, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid Password !!!");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
