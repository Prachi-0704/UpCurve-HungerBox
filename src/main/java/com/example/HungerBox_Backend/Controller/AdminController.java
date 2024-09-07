package com.example.HungerBox_Backend.Controller;

import com.example.HungerBox_Backend.Model.*;
import com.example.HungerBox_Backend.Request.CreateFoodRequest;
import com.example.HungerBox_Backend.Request.CreateVendorRequest;
import com.example.HungerBox_Backend.Response.MessageResponse;
import com.example.HungerBox_Backend.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/")
public class AdminController {

    /** ------------------------  ADMIN FOOD CONTROLLER ---------------------------**/

    @Autowired
    private FoodService foodService;

    @Autowired
    private UserService userService;

    @Autowired
    private VendorService vendorService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EmailSenderService emailSenderService;

    /**
     * Creates a new food item.
     *
     * @param request   the request body containing details of the food to be created
     * @param authHeader the authorization header containing the JWT token
     * @return the created Food entity
     * @throws Exception if there is an error during the creation process
     */
    @PostMapping("/createFood")
    public ResponseEntity<Food> createFood(@RequestBody CreateFoodRequest request,
                                           @RequestHeader("Authorization") String authHeader) throws Exception {
        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User owner = userService.findUserByJwtToken(jwt);
        Vendor vendor = vendorService.getVendorByUserId(owner.getUserId());

        // Create the food item and associate it with the vendor
        Food food = foodService.createFood(request, vendor);

        return new ResponseEntity<>(food, HttpStatus.CREATED);
    }

    /**
     * Deletes a food item by its ID.
     *
     * @param foodId    the ID of the food item to be deleted
     * @param authHeader the authorization header containing the JWT token
     * @return a response message indicating the result of the deletion
     * @throws Exception if there is an error during the deletion process
     */
    @DeleteMapping("/food/delete/{foodId}")
    public ResponseEntity<MessageResponse> deleteFood(@PathVariable long foodId,
                                                      @RequestHeader("Authorization") String authHeader) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Delete the food item
        foodService.deleteFood(foodId);

        // Create and return a response message
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Food deleted successfully");

        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }


    /**
     * Updates multiple details of a food item.
     *
     * @param id            the ID of the food item
     * @param name          the new name of the food item (optional)
     * @param price         the new price of the food item (optional)
     * @param description   the new description of the food item (optional)
     * @return a success or error message
     */
    @PostMapping("/food/update")
    public ResponseEntity<String> updateFoodDetails(@RequestParam("id") Long id,
                                                    @RequestParam(value = "newName", required = false) String name,
                                                    @RequestParam(value = "newPrice", required = false) Integer price,
                                                    @RequestParam(value = "newDescription", required = false) String description) {
        try {
            foodService.updateFoodDetails(id, name, price, description);
            return ResponseEntity.ok("Food details updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update food details. Error: " + e.getMessage());
        }
    }

    /** ------------------------  ADMIN ORDER CONTROLLER ---------------------------**/

    /**
     * Retrieves the order history for a vendor.
     *
     * @param orderStatus the status of orders to filter by (optional)
     * @param authHeader  the authorization header containing the JWT token
     * @return a list of orders for the vendor
     * @throws Exception if there is an error during the retrieval process
     */
    @GetMapping("/orderHistory")
    public ResponseEntity<List<Order>> getOrderHistory(
            @RequestParam(required = false) String orderStatus,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Find the vendor associated with the user
        Vendor vendor = vendorService.getVendorByUserId(user.getUserId());

        // Retrieve the vendor's orders based on the optional status
        List<Order> orders = orderService.getVendorsOrder(vendor.getVendorId(), orderStatus);

        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    /**
     * Updates the status of an order and sends an email notification if the status is "COMPLETED".
     *
     * @param orderId     the ID of the order to be updated
     * @param orderStatus the new status of the order
     * @param authHeader  the authorization header containing the JWT token
     * @return the updated Order entity
     * @throws Exception if there is an error during the update process
     */
    @PutMapping("/order/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(
            @PathVariable long orderId,
            @PathVariable(required = false) String orderStatus,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Get the user who is updating the order status
        User user = userService.findUserByJwtToken(jwt);

        // Update the order status
        Order updatedOrder = orderService.updateOrderStatus(orderId, orderStatus);

        // If the order status is updated to "COMPLETED", send an email notification
        if ("COMPLETED".equalsIgnoreCase(orderStatus)) {
            // Fetch the customer's email and name from the updated order
            String customerEmail = updatedOrder.getCustomer().getEmailId();
            String customerName = updatedOrder.getCustomer().getFullName();

            // Compose the email subject
            String subject = "Order #" + orderId + " is ready!!!";

            // Compose the email body in HTML format
            StringBuilder body = new StringBuilder();
            body.append("<html>")
                    .append("<body>")
                    .append("<h2>Dear ").append(customerName).append(",</h2>")
                    .append("<p>Your order with Order ID: <strong>").append(orderId).append("</strong> is ready to be taken.</p>")
                    .append("<h3>Order Details:</h3>")
                    .append("<table border='1' cellpadding='5' cellspacing='0'>")
                    .append("<thead>")
                    .append("<tr>")
                    .append("<th>Item Name</th>")
                    .append("<th>Quantity</th>")
                    .append("<th>Price</th>")
                    .append("</tr>")
                    .append("</thead>")
                    .append("<tbody>");

            // Loop through the items in the order and add them to the email
            for (OrderItem item : updatedOrder.getItems()) {
                body.append("<tr>")
                        .append("<td>").append(item.getFood().getFoodName()).append("</td>")
                        .append("<td>").append(item.getQuantity()).append("</td>")
                        .append("<td>₹ ").append(item.getTotalPrice()).append("</td>")
                        .append("</tr>");
            }

            body.append("</tbody>")
                    .append("</table>")
                    .append("<p><strong>Total Price: ₹ ").append(updatedOrder.getTotalPrice()).append("</strong></p>")
                    .append("<br/>")
                    .append("<p>Thank you for choosing HungerBox!</p>")
                    .append("<p>Best regards,<br/>HungerBox Team</p>")
                    .append("</body>")
                    .append("</html>");

            // Send the email
            emailSenderService.sendEmail(customerEmail, subject, body.toString());
        }

        // Return the updated order as the response
        return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
    }


    /** ------------------------  ADMIN VENDOR CONTROLLER ---------------------------**/

    /**
     * Creates a new vendor.
     *
     * @param req        the request containing vendor details
     * @param authHeader the authorization header containing the JWT token
     * @return the created Vendor entity
     * @throws Exception if there is an error during vendor creation
     */
    @PostMapping()
    public ResponseEntity<Vendor> createVendor(
            @RequestBody CreateVendorRequest req,
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Create a new vendor
        Vendor vendor = vendorService.createVendor(req, user);

        return new ResponseEntity<>(vendor, HttpStatus.CREATED);
    }

    /**
     * Updates an existing vendor's details.
     *
     * @param req        the request containing updated vendor details
     * @param authHeader the authorization header containing the JWT token
     * @param vendorId   the ID of the vendor to be updated
     * @return the updated Vendor entity
     * @throws Exception if there is an error during vendor update
     */
    @PutMapping("/{vendorId}/update")
    public ResponseEntity<Vendor> updateVendor(
            @RequestBody CreateVendorRequest req,
            @RequestHeader("Authorization") String authHeader,
            @PathVariable long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Update the vendor details
        Vendor vendor = vendorService.updateVendor(vendorId, req);

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    /**
     * Deletes a vendor.
     *
     * @param authHeader the authorization header containing the JWT token
     * @param vendorId   the ID of the vendor to be deleted
     * @return a response message indicating the result of the deletion
     * @throws Exception if there is an error during vendor deletion
     */
    @DeleteMapping("/{vendorId}/delete")
    public ResponseEntity<MessageResponse> deleteVendor(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Delete the vendor
        vendorService.deleteVendor(vendorId);

        MessageResponse response = new MessageResponse();
        response.setMessage("Vendor Deleted Successfully!");

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Updates the status of a vendor.
     *
     * @param authHeader the authorization header containing the JWT token
     * @param vendorId   the ID of the vendor whose status is to be updated
     * @return the updated Vendor entity
     * @throws Exception if there is an error during vendor status update
     */
    @PutMapping("/{vendorId}/status")
    public ResponseEntity<Vendor> updateVendorStatus(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable long vendorId
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User user = userService.findUserByJwtToken(jwt);

        // Update the vendor status
        Vendor vendor = vendorService.updateVendorStatus(vendorId);

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }

    /**
     * Finds a vendor by the user ID associated with the JWT token.
     *
     * @param authHeader the authorization header containing the JWT token
     * @return the Vendor entity associated with the user
     * @throws Exception if there is an error during the retrieval process
     */
    @GetMapping("vendors/user")
    public ResponseEntity<Vendor> findVendorByUserId(
            @RequestHeader("Authorization") String authHeader
    ) throws Exception {

        // Extract the JWT token from the Authorization header
        String jwt = authHeader.replace("Bearer ", "");

        // Find the user by JWT token
        User owner = userService.findUserByJwtToken(jwt);

        // Retrieve the vendor associated with the user
        Vendor vendor = vendorService.getVendorByUserId(owner.getUserId());

        return new ResponseEntity<>(vendor, HttpStatus.OK);
    }
}
