package com.example.HungerBox_Backend.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long orderId;

    @ManyToOne
    private User customer;

    @JsonIgnore
    @ManyToOne
    private Vendor vendor;

    private String orderStatus;

    private LocalDateTime createdAt;

    @OneToMany
    private List<OrderItem> items;

    private int totalItem;

    private long totalPrice;
}
