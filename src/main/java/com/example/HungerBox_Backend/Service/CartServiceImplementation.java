//package com.example.HungerBox_Backend.Service;
//
//import com.example.HungerBox_Backend.Model.Cart;
//import com.example.HungerBox_Backend.Model.CartItem;
//import com.example.HungerBox_Backend.Model.Food;
//import com.example.HungerBox_Backend.Model.User;
//import com.example.HungerBox_Backend.Repository.CartItemRepository;
//import com.example.HungerBox_Backend.Repository.CartRepository;
//import com.example.HungerBox_Backend.Request.AddCartItemRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Optional;
//
//@Service
//public class CartServiceImplementation implements CartService{
//
//    @Autowired
//    CartRepository cartRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    CartItemRepository cartItemRepository;
//
//    @Autowired
//    private FoodService foodService;
//
//    @Override
//    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception {
//
//        User user = userService.findUserByJwtToken(jwt);
//
//        Food food = foodService.findFoodById(request.getFoodId());
//
//        Cart cart = cartRepository.findByCustomerUserId(user.getUserId());
//
//        for(CartItem cartItem : cart.getItems()){
//
//            if(cartItem.getFood().equals(food)){
//
//                int newQuantity = cartItem.getQuantity() + request.getQuantity();
//
//                return updateCartItemQuantity(cartItem.getCartItemId(), newQuantity);
//            }
//        }
//
//        CartItem newCartItem = new CartItem();
//
//        newCartItem.setFood(food);
//
//        newCartItem.setCart(cart);
//
//        newCartItem.setQuantity(request.getQuantity());
//
//        //alternative : use calculateCartTotal(cart)
//        newCartItem.setTotalPrice(request.getQuantity()*food.getPrice());
//
//        CartItem savedCartItem = cartItemRepository.save(newCartItem);
//
//        cart.getItems().add(savedCartItem);
//
//        return savedCartItem;
//    }
//
//    @Override
//    public CartItem updateCartItemQuantity(long cartItemId, int quantity) throws Exception {
//
//        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
//
//        if(cartItemOptional.isEmpty()){
//            throw new Exception("Cart item not found");
//        }
//
//        CartItem item = cartItemOptional.get();
//
//        item.setQuantity(quantity);
//
//        item.setTotalPrice(item.getFood().getPrice()*quantity);
//
//        return cartItemRepository.save(item);
//    }
//
//    @Override
//    public Cart removeItemFromCart(long cartItemId, String jwt) throws Exception {
//
//        User user = userService.findUserByJwtToken(jwt);
//
//        Cart cart = cartRepository.findByCustomerUserId(user.getUserId());
//
//        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);
//
//        if(cartItemOptional.isEmpty()){
//            throw new Exception("Cart item not found");
//        }
//
//        CartItem item = cartItemOptional.get();
//
//        cart.getItems().remove(item);
//
//        return cartRepository.save(cart);
//    }
//
//    @Override
//    public long calculateCartTotal(Cart cart) throws Exception {
//
//        long total = 0;
//
//        for(CartItem cartItem : cart.getItems()){
//            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
//        }
//
//        return total;
//    }
//
//    @Override
//    public Cart findCartByUserId(long userId) throws Exception {
//
//        Cart cart = cartRepository.findByCustomerUserId(userId);
//
//        cart.setTotal(calculateCartTotal(cart));
//
//        return cart;
//    }
//
//    @Override
//    public Cart clearCart(String jwt) throws Exception {
//
//        User user = userService.findUserByJwtToken(jwt);
//
//        Cart cart = findCartByUserId(user.getUserId());
//
//        cart.getItems().clear();
//
//        cart.setTotal(0);
//
//        return cartRepository.save(cart);
//    }
//
//    public Cart deleteCartItem(long cartItemId, String jwt) throws Exception {
//        // Find the user by the JWT token
//        User user = userService.findUserByJwtToken(jwt);
//
//        // Find the cart of the user
//        Cart cart = findCartByUserId(user.getUserId());
//
//        if(cart == null){
//            throw new Exception("Cart not found");
//        }
//
//        // Find the cart item by its ID
//        CartItem cartItem = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new Exception("Cart item not found"));
//
//        // Remove the cart item from the cart
//        cart.getItems().remove(cartItem);
//        cartItemRepository.delete(cartItem);
//
//        // Save the updated cart
//        cartRepository.save(cart);
//
//        return cart;
//    }
//}
































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
public class CartServiceImplementation implements CartService {

    @Autowired
    CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    CartItemRepository cartItemRepository;

    @Autowired
    private FoodService foodService;

    /**
     * Adds an item to the user's cart. If the item already exists in the cart, updates its quantity.
     *
     * @param request the request containing food ID and quantity to add.
     * @param jwt the JWT token of the user.
     * @return the added or updated CartItem.
     * @throws Exception if the user or food item is not found.
     */
    @Override
    public CartItem addItemToCart(AddCartItemRequest request, String jwt) throws Exception {
        // Find the user by the JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Find the food item by ID
        Food food = foodService.findFoodById(request.getFoodId());

        // Find the user's cart
        Cart cart = cartRepository.findByCustomerUserId(user.getUserId());

        // Check if the item is already in the cart
        for (CartItem cartItem : cart.getItems()) {
            if (cartItem.getFood().equals(food)) {
                int newQuantity = cartItem.getQuantity() + request.getQuantity();
                return updateCartItemQuantity(cartItem.getCartItemId(), newQuantity);
            }
        }

        // Create a new CartItem if it doesn't exist
        CartItem newCartItem = new CartItem();
        newCartItem.setFood(food);
        newCartItem.setCart(cart);
        newCartItem.setQuantity(request.getQuantity());
        newCartItem.setTotalPrice(request.getQuantity() * food.getPrice());

        CartItem savedCartItem = cartItemRepository.save(newCartItem);
        cart.getItems().add(savedCartItem);

        return savedCartItem;
    }

    /**
     * Updates the quantity of an existing cart item.
     *
     * @param cartItemId the ID of the cart item to update.
     * @param quantity the new quantity.
     * @return the updated CartItem.
     * @throws Exception if the cart item is not found.
     */
    @Override
    public CartItem updateCartItemQuantity(long cartItemId, int quantity) throws Exception {
        Optional<CartItem> cartItemOptional = cartItemRepository.findById(cartItemId);

        if (cartItemOptional.isEmpty()) {
            throw new Exception("Cart item not found");
        }

        CartItem item = cartItemOptional.get();
        item.setQuantity(quantity);
        item.setTotalPrice(item.getFood().getPrice() * quantity);

        return cartItemRepository.save(item);
    }

    /**
     * Calculates the total price of the items in the cart.
     *
     * @param cart the Cart object.
     * @return the total price of all items in the cart.
     * @throws Exception if any error occurs during calculation.
     */
    @Override
    public long calculateCartTotal(Cart cart) throws Exception {
        long total = 0;
        for (CartItem cartItem : cart.getItems()) {
            total += cartItem.getFood().getPrice() * cartItem.getQuantity();
        }
        return total;
    }

    /**
     * Finds the cart for a specific user and updates its total price.
     *
     * @param userId the ID of the user.
     * @return the Cart object with updated total price.
     * @throws Exception if the cart is not found.
     */
    @Override
    public Cart findCartByUserId(long userId) throws Exception {
        Cart cart = cartRepository.findByCustomerUserId(userId);
        cart.setTotal(calculateCartTotal(cart));
        return cart;
    }

    /**
     * Clears all items from the user's cart and sets the total price to zero.
     *
     * @param jwt the JWT token of the user.
     * @return the updated Cart object with no items.
     * @throws Exception if the user or cart is not found.
     */
    @Override
    public Cart clearCart(String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = findCartByUserId(user.getUserId());
        cart.getItems().clear();
        cart.setTotal(0);
        return cartRepository.save(cart);
    }

    /**
     * Deletes a specific cart item and updates the cart.
     *
     * @param cartItemId the ID of the cart item to delete.
     * @param jwt the JWT token of the user.
     * @return the updated Cart.
     * @throws Exception if the user, cart, or cart item is not found.
     */
    public Cart deleteCartItem(long cartItemId, String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Cart cart = findCartByUserId(user.getUserId());

        if (cart == null) {
            throw new Exception("Cart not found");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new Exception("Cart item not found"));

        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);

        return cartRepository.save(cart);
    }
}