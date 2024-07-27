package com.example.HungerBox_Backend.Repository;

import com.example.HungerBox_Backend.Model.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("SELECT v FROM Vendor v WHERE lower(v.vendorName) LIKE lower(concat('%', :query, '%'))" +
            "OR lower(v.cuisineType) LIKE lower(concat('%', :query, '%'))")
    List<Vendor> findBySearchQuery(String query);

    Vendor findByOwnerUserId(long userId);
}
