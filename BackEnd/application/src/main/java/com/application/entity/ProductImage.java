package com.application.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product_image")

public class ProductImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "handle", nullable = false)
    private String handle;
    @Column(name = "position", nullable = false)
    private int position;
    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;
    @Column(name = "altText")
    private String altText;
}