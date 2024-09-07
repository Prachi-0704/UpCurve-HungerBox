package com.example.HungerBox_Backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    @JsonIgnore // Exclude this from serialization to avoid recursion
    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "vendor_id")  // Make sure the column name matches your database schema
    private Vendor vendor;

    private String orderStatus;

    private LocalDateTime createdAt;

    //@JsonIgnore  // Exclude this to prevent recursion
    @OneToMany
//    @OneToMany(fetch = FetchType.EAGER, mappedBy = "order")
    private List<OrderItem> items;

    private int totalItem;

    private long totalPrice;

    @ManyToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    @ToString.Exclude  // Exclude this to prevent recursion
    @JoinColumn(name = "customer_order_id")
    private CustomerOrder customerOrder;
}
