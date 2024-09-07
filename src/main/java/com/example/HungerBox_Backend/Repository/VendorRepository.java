package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for handling CRUD operations related to Vendor entities.
 */
@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    /**
     * Finds a list of Vendor entities based on a search query that matches the vendor name or cuisine type.
     *
     * @param query the search query string to match against vendor name or cuisine type
     * @return a list of Vendor entities that match the search query
     */
    @Query("SELECT v FROM Vendor v WHERE lower(v.vendorName) LIKE lower(concat('%', :query, '%'))" +
            "OR lower(v.cuisineType) LIKE lower(concat('%', :query, '%'))")
    List<Vendor> findBySearchQuery(String query);

    /**
     * Finds a Vendor entity by the owner user ID.
     *
     * @param userId the ID of the user who owns the vendor
     * @return the Vendor entity associated with the specified user ID, or null if not found
     */
    Vendor findByOwnerUserId(long userId);
}
