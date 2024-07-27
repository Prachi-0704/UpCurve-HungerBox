package com.example.HungerBox_Backend.DTO;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class VendorDTO {
    private String title;

    @Column(length = 1000)
    private List<String> images;


    private String description;
    private long vendorId;

}
