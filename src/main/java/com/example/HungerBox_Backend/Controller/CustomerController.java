package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.*;
import com.example.HungerBox_Backend.Repository.CustomerOrderRepository;
import com.example.HungerBox_Backend.Repository.OrderItemRepository;
import com.example.HungerBox_Backend.Repository.OrderRepository;
import com.example.HungerBox_Backend.Repository.WalletRepository;
import com.example.HungerBox_Backend.Request.AddCartItemRequest;
import com.example.HungerBox_Backend.Request.UpdateCartItemRequest;
import com.example.HungerBox_Backend.Response.MessageResponse;
import com.example.HungerBox_Backend.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/")
public class CustomerController {
    /**-------------------------CUSTOMER FOOD CONTROLLER ---------------------------**/

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    /**
     * Searches for food items based on a keyword.
     *
     * @param keyword   the keyword to search for
     * @param authHeader the JWT token
     * @return response with the list of food items matching the keyword
     * @throws Exception if an error occurs
     */
    @GetMapping("/food/search")
    public ResponseEntity<List<Food>> searchFood(
            @RequestParam String keyword,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Search for food items
        List<Food> foods = foodService.searchFood(keyword);

        return new ResponseEntity<>(foods, HttpStatus.OK);
    }

    /**
     * Retrieves food items from a specific vendor.
     *
     * @param vendorId the ID of the vendor
     * @param veg      whether to include vegetarian items
     * @param nonVeg   whether to include non-vegetarian items
     * @param authHeader the JWT token
     * @return response with the list of food items from the vendor
     * @throws Exception if an error occurs
     */
    @GetMapping("/food/vendor/{vendorId}")
    public ResponseEntity<List<Food>> getVendorFood(
            @PathVariable long vendorId,
            @RequestParam boolean veg,
            @RequestParam boolean nonVeg,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Retrieve food items from the specified vendor
        List<Food> foods = foodService.getVendorFood(vendorId, veg, nonVeg);

        return new ResponseEntity<>(foods, HttpStatus.OK);
    }


    /**-------------------------CUSTOMER VENDOR CONTROLLER ---------------------------**/

    /**
     * Search vendors by keyword.
     * This endpoint allows customers to search for vendors using a keyword.
     * The keyword can match vendor names or other searchable fields.
     *
     * @param authHeader - Authorization header containing the JWT token.
     * @param keyword - The search keyword to find matching vendors.
     * @return List of vendors matching the search keyword.
     * @throws Exception if any error occurs during the search process.
     **/
    @GetMapping("vendors/search")
    public ResponseEntity<List<Vendor>> searchVendor(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam String keyword
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token (verifying the customer is authenticated)
        User user = userService.findUserByJwtToken(jwt);

        // Search for vendors based on the provided keyword
        List<Vendor> vendor = vendorService.searchVendor(keyword);

        // Return the list of vendors with OK status
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    /**
     * Get all available vendors.
     * This endpoint retrieves a list of all vendors in the system.
     *
     * @param authHeader - Authorization header containing the JWT token.
     * @return List of all vendors.
     * @throws Exception if any error occurs while fetching vendors.
     **/
    @GetMapping("/vendors")
    public ResponseEntity<List<Vendor>> getAllVendor(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token (verifying the customer is authenticated)
        User user = userService.findUserByJwtToken(jwt);

        // Retrieve all vendors from the vendor service
        List<Vendor> vendor = vendorService.getAllVendor();

        // Return the list of vendors with OK status
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    /**
     * Find a vendor by its ID.
     * This endpoint allows customers to fetch a specific vendor's details by vendor ID.
     *
     * @param authHeader - Authorization header containing the JWT token.
     * @param vendorId - The ID of the vendor to fetch.
     * @return The vendor with the specified ID.
     * @throws Exception if the vendor is not found or other errors occur.
     **/
    @GetMapping("/vendors/{vendorId}")
    public ResponseEntity<Vendor> findVendorById(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token (verifying the customer is authenticated)
        User user = userService.findUserByJwtToken(jwt);

        // Retrieve the vendor by its ID
        Vendor vendor = vendorService.findVendorById(vendorId);

        // Return the vendor with OK status
        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }


    /**------------------------- CART CONTROLLER ---------------------------**/

    @Autowired
    private CartService cartService;

    /**
     * Adds an item to the cart.
     *
     * @param request   the request containing item details
     * @param authHeader the JWT token
     * @return response with added cart item
     * @throws Exception if an error occurs
     */
    @PutMapping("/cart/add")
    public ResponseEntity<CartItem> addItemToCart(
            @RequestBody AddCartItemRequest request,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        CartItem cartItem = cartService.addItemToCart(request, jwt);

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    /**
     * Updates the quantity of an item in the cart.
     *
     * @param request   the request containing cart item ID and new quantity
     * @param authHeader the JWT token
     * @return response with updated cart item
     * @throws Exception if an error occurs
     */
    @PutMapping("/cart-item/update")
    public ResponseEntity<CartItem> updateCartItemQuantity(
            @RequestBody UpdateCartItemRequest request,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        CartItem cartItem = cartService.updateCartItemQuantity(request.getCartItemId(), request.getQuantity());

        return new ResponseEntity<>(cartItem, HttpStatus.OK);
    }

    /**
     * Clears all items from the cart.
     *
     * @param authHeader the JWT token
     * @return response with cleared cart
     * @throws Exception if an error occurs
     */
    @DeleteMapping("/cart/clear")
    public ResponseEntity<Cart> clearCart(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        Cart cart = cartService.clearCart(jwt);

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Retrieves the cart for the authenticated user.
     *
     * @param authHeader the JWT token
     * @return response with the user's cart
     * @throws Exception if an error occurs
     */
    @GetMapping("/cart")
    public ResponseEntity<Cart> findUserCart(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        User user = userService.findUserByJwtToken(jwt);

        Cart cart = cartService.findCartByUserId(user.getUserId());

        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    /**
     * Deletes an item from the cart.
     *
     * @param cartItemId the ID of the cart item to delete
     * @param authHeader the JWT token
     * @return response with a deletion message
     * @throws Exception if an error occurs
     */
    @DeleteMapping("/cart-item/delete/{cartItemId}")
    public ResponseEntity<MessageResponse> deleteCartItem(
            @PathVariable long cartItemId,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        cartService.deleteCartItem(cartItemId, jwt);

        MessageResponse response = new MessageResponse();
        response.setMessage("Cart Item Deleted Successfully!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    /**------------------------- CUSTOMER ORDER CONTROLLER ---------------------------**/

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    @Autowired
    private WalletService walletService;

    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    @Autowired
    CustomerOrderService customerOrderService;

    /**
     * Create Order for the User.
     * This endpoint allows users to create an order based on the items in their cart.
     * The order is grouped by vendors, and an order is placed for each vendor.
     * The total price is deducted from the user's wallet.
     *
     * @param authHeader - Authorization header containing the JWT token.
     * @return Created CustomerOrder.
     * @throws Exception if any errors occur such as insufficient funds or empty cart.
     */
    @PutMapping("/order")
    public ResponseEntity<CustomerOrder> createOrder(
            @RequestHeader("Authorization") String authHeader) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Retrieve the user based on the JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Retrieve the cart items for the user
        Cart cart = cartService.findCartByUserId(user.getUserId());

        // Ensure the cart is not empty
        if (cart.getItems().isEmpty()) {
            throw new Exception("The cart is empty. Cannot create an order.");
        }

        // Map to hold orders grouped by vendors
        Map<Vendor, List<OrderItem>> ordersByVendor = new HashMap<>();

        // Group cart items by vendor and create order items
        for (CartItem cartItem : cart.getItems()) {
            Vendor vendor = cartItem.getFood().getVendor();
            OrderItem orderItem = new OrderItem();
            orderItem.setFood(cartItem.getFood());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getTotalPrice());

            // Save each order item to the database
            orderItemRepository.save(orderItem);

            ordersByVendor
                    .computeIfAbsent(vendor, k -> new ArrayList<>())
                    .add(orderItem);
        }

        // List to hold all created orders
        List<Order> savedOrders = new ArrayList<>();

        // Calculate the total amount across all vendors' orders
        long grandTotalPrice = ordersByVendor.values().stream()
                .flatMap(List::stream)
                .mapToLong(OrderItem::getTotalPrice)
                .sum();

        // Retrieve the user's wallet and ensure sufficient balance
        Wallet wallet = walletService.getOrCreateWallet(user.getUserId());
        if (wallet.getBalance() < grandTotalPrice) {
            throw new Exception("createOrder() -> Insufficient funds in wallet to place the order.");
        }

        // Deduct the total price from the wallet
        wallet.setBalance(wallet.getBalance() - grandTotalPrice);
        walletRepository.save(wallet);  // Save the updated wallet balance

        // Create a new CustomerOrder entity
        CustomerOrder customerOrder = new CustomerOrder();
        customerOrder.setCustomer(user);
        customerOrder.setTotalPrice(grandTotalPrice);
        customerOrder.setCreatedAt(LocalDateTime.now());

        // Create an order for each vendor and associate with CustomerOrder
        for (Map.Entry<Vendor, List<OrderItem>> entry : ordersByVendor.entrySet()) {
            Vendor vendor = entry.getKey();
            List<OrderItem> orderItems = entry.getValue();

            Order newOrder = new Order();
            newOrder.setCustomer(user);
            newOrder.setCreatedAt(LocalDateTime.now());
            newOrder.setOrderStatus("PENDING");
            newOrder.setItems(orderItems);

            // Calculate total price for this vendor's order
            long totalPrice = orderItems.stream()
                    .mapToLong(OrderItem::getTotalPrice)
                    .sum();
            newOrder.setTotalPrice(totalPrice);

            // Associate the vendor with this order
            newOrder.setVendor(vendor);

            // Associate the order with the customerOrder
            newOrder.setCustomerOrder(customerOrder);

            // Save the order in the database
            Order savedOrder = orderRepository.save(newOrder);
            savedOrders.add(savedOrder);
        }

        // Set the list of orders in the customerOrder entity
        customerOrder.setOrders(savedOrders);

        // Save the customerOrder in the database
        CustomerOrder savedCustomerOrder = customerOrderRepository.save(customerOrder);

        // Optionally, clear the user's cart after creating the orders
        cartService.clearCart(jwt);

        // Return the created CustomerOrder entity
        return new ResponseEntity<>(savedCustomerOrder, HttpStatus.OK);
    }

    /**
     * Get Order History for the User.
     * This endpoint allows customers to fetch their order history.
     *
     * @param authHeader - Authorization header containing the JWT token.
     * @return List of CustomerOrder representing the user's order history.
     * @throws Exception if any error occurs during retrieval.
     */
    @GetMapping("/order")
    public ResponseEntity<List<CustomerOrder>> getOrderHistory(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user associated with the JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Fetch CustomerOrder objects by userId
        List<CustomerOrder> customerOrders = customerOrderService.getCustomerOrdersByUserId(user.getUserId());

        // Return the list of CustomerOrder objects
        return new ResponseEntity<>(customerOrders, HttpStatus.OK);
    }

    /**
     * Get Orders for a Vendor.
     * This endpoint allows a vendor to fetch their orders, with an optional filter by order status.
     *
     * @param authHeader - Authorization header containing the JWT token.
     * @param status - Optional filter for the order status.
     * @return List of Orders for the vendor.
     * @throws Exception if the user is not a vendor or if an error occurs during retrieval.
     */
    @GetMapping("/vendor/orders")
    public ResponseEntity<List<Order>> getVendorOrders(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam(value = "status", required = false) String status
    ) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Retrieve the vendor information using the JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Validate if the user is a vendor (role = 1)
        if (user.getRole() != 1) {
            throw new Exception("Unauthorized Access");
        }

        // Get Vendor by userId
        Vendor vendor = vendorService.getVendorByUserId(user.getUserId());

        // Get the vendor's orders
        List<Order> vendorOrders = orderService.getVendorsOrder(vendor.getVendorId(), status);

        return new ResponseEntity<>(vendorOrders, HttpStatus.OK);
    }

    /**
     * Cancel a Customer Order.
     * This endpoint allows a customer to cancel their order and receive a refund.
     * The amount is reimbursed to the customer's wallet upon cancellation.
     *
     * @param customerOrderId - The ID of the CustomerOrder to be canceled.
     * @return A message confirming the cancellation and reimbursement.
     * @throws Exception if the order is not found or an error occurs during cancellation.
     */
    @DeleteMapping("/user/cancelCustomerOrder/{customerOrderId}")
    public ResponseEntity<String> cancelCustomerOrder(@PathVariable long customerOrderId) throws Exception {
        // Find the CustomerOrder by customerOrderId
        CustomerOrder customerOrder = customerOrderService.findCustomerOrderById(customerOrderId);

        if (customerOrder == null) {
            return new ResponseEntity<>("Customer order not found", HttpStatus.NOT_FOUND);
        }

        // Find the associated user from the order
        User user = customerOrder.getCustomer();

        // Calculate the total price of the canceled order
        double refundAmount = customerOrder.getTotalPrice();

        // Add the refund amount back to the user's wallet
        walletService.addBalance(user.getUserId(), refundAmount);

        // Delete the CustomerOrder and associated Orders
        customerOrderService.deleteCustomerOrder(customerOrder);

        return new ResponseEntity<>("Customer order canceled and amount reimbursed successfully", HttpStatus.OK);
    }

    /** -------------------------------- WALLET CONTROLLER ---------------------------**/

    /**
     * Retrieve the wallet for the current user.
     *
     * @param authHeader Authorization header containing JWT token.
     * @return Wallet entity.
     */
    @GetMapping("wallet/get")
    public Wallet getWallet(
            @RequestHeader("Authorization") String authHeader) throws Exception {

        // Extract JWT token
        String jwt = authHeader.replace("Bearer ", "");

        // Retrieve user
        User user = userService.findUserByJwtToken(jwt);

        // Get or create wallet
        return walletService.getOrCreateWallet(user.getUserId());
    }


    /**
     * Find User by JWT Token
     * This endpoint retrieves user details based on the JWT token provided in the Authorization header.
     *
     * @param authHeader - Authorization header containing the JWT token.
     * @return User object wrapped in a ResponseEntity with HTTP status.
     */
    @GetMapping("/profile")
    public ResponseEntity<?> findUserByJwtToken(@RequestHeader("Authorization") String authHeader) {
        try {
            // Extract the JWT token from the Authorization header
            String jwt = authHeader.replace("Bearer ", "");

            // Find the user using the JWT token
            User user = userService.findUserByJwtToken(jwt);

            // Return the user details with an OK (200) status if found successfully
            return new ResponseEntity<>(user, HttpStatus.OK);
        }
        catch (Exception e) {
            // In case of any exception, return the error message with a 500 Internal Server Error status
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
