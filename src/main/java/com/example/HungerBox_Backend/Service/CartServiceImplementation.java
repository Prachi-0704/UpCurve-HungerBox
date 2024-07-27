package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.Cart;
import com.example.HungerBox_Backend.Model.CartItem;
import com.example.HungerBox_Backend.Model.Food;
import com.example.HungerBox_Backend.Model.User;
import com.example.HungerBox_Backend.Repository.CartItemRepository;
import com.example.HungerBox_Backend.Repository.CartRepository;
import com.example.HungerBox_Backend.Request.AddCartItemRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImplementation implements CartService{

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private FoodService foodService;

    @Override
    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Food food = foodService.findFoodById(request.getFoodId());

        Cart cart = cartRepository.findByCustomerUserId(user.getUserId());

        for(CartItem cartItem : cart.getItems()){

            if(cartItem.getFood().equals(food)){

                int newQuantity = cartItem.getQuantity() + request.getQuantity();

                return updateCartItemQuantity(cartItem.getCartItemId(), newQuantity);
            }
        }

        CartItem newCartItem = new CartItem();

        newCartItem.setFood(food);

        newCartItem.setCart(cart);

        newCartItem.setQuantity(request.getQuantity());

        //alternative : use calculateCartTotal(cart)
        newCartItem.setTotalPrice(request.getQuantity()*food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);

        cart.getItems().add(savedCartItem);

        return savedCartItem;
    }

    @Override
    public CartItem updateCartItemQuantity(long cartItemId, int quantity) throws Exception {

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if(cartItemOptional.isEmpty()){
            throw new Exception("Cart item not found");
        }

        CartItem item = cartItemOptional.get();

        item.setQuantity(quantity);

        item.setTotalPrice(item.getFood().getPrice()*quantity);

        return cartItemRepository.save(item);
    }

    @Override
    public Cart removeItemFromCart(long cartItemId, String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartRepository.findByCustomerUserId(user.getUserId());

        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if(cartItemOptional.isEmpty()){
            throw new Exception("Cart item not found");
        }

        CartItem item = cartItemOptional.get();

        cart.getItems().remove(item);

        return cartRepository.save(cart);
    }

    @Override
    public long calculateCartTotal(Cart cart) throws Exception {

        long total = 0;

        for(CartItem cartItem : cart.getItems()){
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }

        return total;
    }

    @Override
    public Cart findCartByUserId(long userId) throws Exception {

        Cart cart = cartRepository.findByCustomerUserId(userId);

        cart.setTotal(calculateCartTotal(cart));

        return cart;
    }

    @Override
    public Cart clearCart(String jwt) throws Exception {

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = findCartByUserId(user.getUserId());

        cart.getItems().clear();

        cart.setTotal(0);

        return cartRepository.save(cart);
    }
}
