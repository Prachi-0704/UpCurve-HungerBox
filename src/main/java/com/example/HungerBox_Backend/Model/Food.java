package com.example.HungerBox_Backend.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Food {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long foodId;

    private String foodName;

    private String description;

    private long price;

    @Column(length = 1000)
    @ElementCollection  //will create separate table for food images
    private List<String> images;

    private boolean available;

    @ManyToOne
    private Vendor vendor;

    private boolean isVeg;

    private boolean isNonVeg;

    private long calories;

    private LocalDateTime creationDate;

}
