//package com.example.HungerBox_Backend.Service;
//
//import com.example.HungerBox_Backend.Model.CustomerOrder;
//import com.example.HungerBox_Backend.Repository.CustomerOrderRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class CustomerOrderService {
//
//    @Autowired
//    private CustomerOrderRepository customerOrderRepository;
//
//    // Method to get CustomerOrder by userId
//    public List<CustomerOrder> getCustomerOrdersByUserId(long userId) {
//        return customerOrderRepository.findByCustomerUserId(userId);
//    }
//
//    public CustomerOrder findCustomerOrderById(long customerOrderId) {
//        return customerOrderRepository.findById(customerOrderId).orElse(null);
//    }
//
//    public void deleteCustomerOrder(CustomerOrder customerOrder) {
//        customerOrderRepository.delete(customerOrder);
//    }
//}
































package com.example.HungerBox_Backend.Service;

import com.example.HungerBox_Backend.Model.CustomerOrder;
import com.example.HungerBox_Backend.Repository.CustomerOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerOrderService {

    @Autowired
    private CustomerOrderRepository customerOrderRepository;

    /**
     * Retrieves a list of customer orders by the user ID.
     *
     * @param userId the ID of the customer user.
     * @return a list of CustomerOrder objects associated with the user.
     */
    public List<CustomerOrder> getCustomerOrdersByUserId(long userId) {
        return customerOrderRepository.findByCustomerUserId(userId);
    }

    /**
     * Finds a customer order by its ID.
     *
     * @param customerOrderId the ID of the customer order.
     * @return the CustomerOrder object if found, or null if not found.
     */
    public CustomerOrder findCustomerOrderById(long customerOrderId) {
        return customerOrderRepository.findById(customerOrderId).orElse(null);
    }

    /**
     * Deletes a specific customer order.
     *
     * @param customerOrder the customer order to be deleted.
     */
    public void deleteCustomerOrder(CustomerOrder customerOrder) {
        customerOrderRepository.delete(customerOrder);
    }
}
