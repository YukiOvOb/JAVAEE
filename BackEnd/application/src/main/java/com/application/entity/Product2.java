package com.application.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.*;

@Entity
@Getter
@Setter
public class Product2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String description;
    private String name;
    private double unitPrice;
    private int stock;
    private String category;
    private String brand;
    private String collection;
    private String imageUrl;
    private String imageAlt;

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems;

    public Product2() {
    }
}
