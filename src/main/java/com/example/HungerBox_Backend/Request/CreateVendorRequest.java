package com.example.HungerBox_Backend.Request;

import com.example.HungerBox_Backend.Model.ContactInformation;
import lombok.Data;

import java.util.List;

@Data
public class CreateVendorRequest {

    private long vendorId;

    private String vendorName;

    private String description;

    private String cuisineType;

    private ContactInformation contactInformation;

    private String openingHours;

    private List<String> images;
}
