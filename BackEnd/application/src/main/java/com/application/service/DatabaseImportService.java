//this one is for importing data from csv files to database
// we will import data from 3 csv files: jewelery.csv, apparel.csv, home-and-garden.csv
// each file contains products from a specific category
// we will import products to products table, and product images to product_images table


// package com.example.shopdemo.service;

// import com.example.shopdemo.tools.ShopifyCsvExtractor;
// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.stereotype.Service;
// import jakarta.annotation.PostConstruct;

// import java.math.BigDecimal;
// import java.util.ArrayList;
// import java.util.HashSet;
// import java.util.List;
// import java.util.Set;

// @Service
// public class DatabaseImportService {

//     private final JdbcTemplate jdbcTemplate;

//     public DatabaseImportService(JdbcTemplate jdbcTemplate) {
//         this.jdbcTemplate = jdbcTemplate;
//     }

//     @PostConstruct
//     public void importAll() throws Exception {
//         // create tables if not exist
//         jdbcTemplate.update("""
//             CREATE TABLE IF NOT EXISTS products (
//               id BIGINT AUTO_INCREMENT PRIMARY KEY,
//               handle VARCHAR(255) NOT NULL UNIQUE,
//               title VARCHAR(255),
//               description TEXT,
//               image_url TEXT,
//               price DECIMAL(10,2),
//               brand VARCHAR(255),
//               category VARCHAR(255),
//               collection VARCHAR(255),
//               stock INT
//             ) CHARACTER SET utf8mb4
//         """);
//         jdbcTemplate.update("""
//             CREATE TABLE IF NOT EXISTS product_images (
//               id BIGINT AUTO_INCREMENT PRIMARY KEY,
//               handle VARCHAR(255) NOT NULL,
//               position INT,
//               image_url TEXT,
//               alt_text VARCHAR(255)
//             ) CHARACTER SET utf8mb4
//         """);

//         ShopifyCsvExtractor.Result jewelery = ShopifyCsvExtractor.extract("data/jewelery.csv", "jewelery");
//         ShopifyCsvExtractor.Result apparel = ShopifyCsvExtractor.extract("data/apparel.csv", "apparel");
//         ShopifyCsvExtractor.Result home = ShopifyCsvExtractor.extract("data/home-and-garden.csv", "home-and-garden");

//         List<ShopifyCsvExtractor.ProductRow> products = new ArrayList<>();
//         products.addAll(jewelery.products);
//         products.addAll(apparel.products);
//         products.addAll(home.products);

//         List<ShopifyCsvExtractor.ProductImageRow> images = new ArrayList<>();
//         images.addAll(jewelery.images);
//         images.addAll(apparel.images);
//         images.addAll(home.images);

//         // upsert products by handle
//         jdbcTemplate.batchUpdate(
//             "INSERT INTO products (handle,title,description,image_url,price,brand,category,collection,stock) " +
//             "VALUES (?,?,?,?,?,?,?,?,?) " +
//             "ON DUPLICATE KEY UPDATE title=VALUES(title),description=VALUES(description),image_url=VALUES(image_url)," +
//             "price=VALUES(price),brand=VALUES(brand),category=VALUES(category),collection=VALUES(collection),stock=VALUES(stock)",
//             products,
//             100,
//             (ps, p) -> {
//                 ps.setString(1, p.handle);
//                 ps.setString(2, p.title);
//                 ps.setString(3, p.description);
//                 ps.setString(4, p.imageUrl);
//                 ps.setBigDecimal(5, parseDecimal(p.price));
//                 ps.setString(6, p.brand);
//                 ps.setString(7, p.category);
//                 ps.setString(8, p.collection);
//                 ps.setInt(9, p.stock);
//             }
//         );

//         // delete old images per handle then insert fresh
//         Set<String> handles = new HashSet<>();
//         for (ShopifyCsvExtractor.ProductRow p : products) handles.add(p.handle);
//         for (String h : handles) {
//             jdbcTemplate.update("DELETE FROM product_images WHERE handle = ?", h);
//         }
//         jdbcTemplate.batchUpdate(
//             "INSERT INTO product_images (handle,position,image_url,alt_text) VALUES (?,?,?,?)",
//             images,
//             200,
//             (ps, i) -> {
//                 ps.setString(1, i.handle);
//                 ps.setInt(2, i.position);
//                 ps.setString(3, i.imageUrl);
//                 ps.setString(4, i.altText);
//             }
//         );
//     }

//     private static BigDecimal parseDecimal(String s) {
//         try { return new BigDecimal(s.trim()); } catch (Exception e) { return BigDecimal.ZERO; }
//     }
// }