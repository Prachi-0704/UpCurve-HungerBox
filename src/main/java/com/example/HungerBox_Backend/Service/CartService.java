package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Cart;
import com.example.HungerBox_Backend.Model.CartItem;
import com.example.HungerBox_Backend.Request.AddCartItemRequest;

public interface CartService {
    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception;

    public CartItem updateCartItemQuantity(long cartItemId, int quantity) throws Exception;

    public long calculateCartTotal(Cart cart) throws Exception;

    public Cart findCartByUserId(long userId) throws Exception;

    public Cart clearCart(String jwt) throws Exception;

    public Cart deleteCartItem(long cartItemId, String jwt) throws Exception;
}
