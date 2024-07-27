package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.Cart;
import com.example.HungerBox_Backend.Model.CartItem;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Request.AddCartItemRequest;
import com.example.HungerBox_Backend.Request.UpdateCartItemRequest;
import com.example.HungerBox_Backend.Service.CartService;
import com.example.HungerBox_Backend.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private UserService userService;

    /**TESTED**/
    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(@RequestBody AddCartItemRequest request,
                                                  @RequestHeader("Authorization") String authHeader
    )throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        CartItem cartItem = cartService.addItemToCart(request, jwt);

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    /**TESTED**/
    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest request,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        CartItem cartItem = cartService.updateCartItemQuantity(request.getCartItemId(), request.getQuantity());

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }


    /**TESTED**/
    @DeleteMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        Cart cart = cartService.clearCart(jwt);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**TESTED**/
    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception{

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findCartByUserId(user.getUserId());

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}
