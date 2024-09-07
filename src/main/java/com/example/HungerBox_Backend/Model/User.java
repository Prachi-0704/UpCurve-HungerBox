//package com.example.HungerBox_Backend.Model;
//
//import com.example.HungerBox_Backend.DTO.VendorDTO;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonProperty;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Table(name = "user")
//public class User {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private long userId;
//
//    private String fullName;
//
//    @Column(unique = true, nullable = false)
//    private String emailId;
//
//    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//    private String password;
//
//    @Enumerated(EnumType.STRING)
//    private USER_ROLE role = USER_ROLE.ROLE_CUSTOMER;
//
//    @JsonIgnore
//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
//    private List<Order> userOrders = new ArrayList<>();
//}










package com.example.HungerBox_Backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userId;

    private String fullName;

    @Column(unique = true, nullable = false)
    private String emailId;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    // 0 --> Customer
    // 1 --> Vendor
    private int role = 0;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private List<Order> userOrders = new ArrayList<>();

    // New fields for login date tracking
    private LocalDate previousLoginDate;
    private LocalDate currentLoginDate;
}
