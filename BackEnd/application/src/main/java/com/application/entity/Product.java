package com.application.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    // handle can also be used as a unique identifier
    private String handle;

    // private String title;

    private String description;

    private String imageUrl;

    private String imageAlt;

    private BigDecimal price;

    private Double unitPrice;

    private String brand;

    private String category;

    private String collection;

    private int stock;
} 