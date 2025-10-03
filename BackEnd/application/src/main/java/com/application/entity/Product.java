package com.application.entity;

import java.math.BigDecimal;
import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // handle actually can also be used as a unique identifier,
    // but we still keep id as well for convenience
    @Column(name = "handle", nullable = false)
    private String handle;
    
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "imageUrl", nullable = false)    
    private String imageUrl;
    @Column(name = "price", nullable = false)
    private BigDecimal price;
    @Column(name = "brand", nullable = false)
    private String brand;
    @Column(name = "category")
    private String category;
    @Column(name = "collection", nullable = false)  
    private String collection;
    @Column(name = "stock", nullable = false)
    private int stock;

    private String name;

    
}
// testing pls ignore this 