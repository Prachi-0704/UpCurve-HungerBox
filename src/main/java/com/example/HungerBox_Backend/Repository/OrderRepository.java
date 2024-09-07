package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Order;
import com.example.HungerBox_Backend.Model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling CRUD operations related to Order entities.
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    /**
     * Finds all orders associated with a specific vendor using a custom query.
     *
     * @param vendorId the ID of the vendor for whom to find orders
     * @return a list of Order entities associated with the specified vendor
     */
    @Query("SELECT o FROM Order o WHERE o.vendor.vendorId = :vendorId")
    List<Order> findOrdersByVendorId(@Param("vendorId") long vendorId);

    /**
     * Finds an order that contains a specific OrderItem.
     *
     * @param orderItem the OrderItem to find within orders
     * @return an Order entity that contains the specified OrderItem
     */
    @Query("SELECT o FROM Order o JOIN o.items oi WHERE oi = :orderItem")
    Order findByItemsContaining(@Param("orderItem") OrderItem orderItem);
}
