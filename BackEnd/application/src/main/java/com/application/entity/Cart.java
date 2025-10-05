package com.application.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(mappedBy = "cart")
    private List<CartItem> cartItems;

    @OneToOne // owner of FK
    @JoinColumn(name = "customer_username")
    private Customer customer;

    public Cart() {
    }
}
