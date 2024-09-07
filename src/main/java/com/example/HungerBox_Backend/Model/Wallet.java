package com.example.HungerBox_Backend.Model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private User user;

    private double balance;

    private String pin;
}
