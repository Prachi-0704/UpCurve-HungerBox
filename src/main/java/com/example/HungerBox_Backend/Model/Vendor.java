package com.example.HungerBox_Backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long vendorId;

    @OneToOne
    private User owner;

    private String vendorName;

    private String description;

    private String cuisineType;

    @Embedded
    private ContactInformation contactInformation;

    private String openingHours;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL, orphanRemoval = true) //"vendor" attribute is in Order Entity Class
    private List<Order> vendorOrders = new ArrayList<>();

    @ElementCollection
    @Column(length = 1000)
    private List<String> images;

    private LocalDateTime registrationDate;

    private boolean open;

    @JsonIgnore
    // when the Vendor is deleted from the DB, all it's food items will also get deleted
    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Food> foods = new ArrayList<>();
}
