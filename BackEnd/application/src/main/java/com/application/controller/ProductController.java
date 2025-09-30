package com.application.controller;

import com.application.service.*;
import com.application.entity.*;
import com.application.utility.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@CrossOrigin
@RequestMapping("/api/products")
public class ProductController {
    @Autowired
    private ProductService productService;

    @PostMapping("/productsfindbyid")
    public Result<Product> findById(@RequestBody Map<String, Integer> request) {
        Integer id = request.get("id");
        Product product = productService.findById(id);
        if (product != null) {
            return Result.success(product);
        } else {
            return Result.error("Product not found");
        }
    }

    @PostMapping("/productsfindbycategory")
    public Result<List<Product>> findByCategory(@RequestBody Map<String, String> request) {
        String category = request.get("category");
        System.out.println(category);
        List<Product> products = productService.findByCategory(category);
        System.out.println("Search Result: " + products);
        if (products != null && !products.isEmpty()) {
            return Result.success(products);
        } else {
            return Result.error("No products found");
        }
    }

    @PostMapping("/productsfindbyname")
    public Result<List<Product>> findByName(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        // System.out.println("ReceivedName: " + name);
        try {
            List<Product> products = productService.findByName(name);
            // System.out.println("findByNameResult: " + (products != null ? products.size()
            // : 0) + "
            // ");
            if (products != null && !products.isEmpty()) {
                return Result.success(products);
            } else {
                return Result.error("No products found with name: " + name);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("Server error: " + e.getMessage());
        }
    }

    // 获取所有产品
    @GetMapping("/all")
    public Result<List<Product>> getAllProducts() {
        try {
            List<Product> products = productService.findAll();
            return Result.success(products);
        } catch (Exception e) {
            return Result.error("Server error: " + e.getMessage());
        }
    }

    @PostMapping("productdeletebyid")
    public Result<String> productdeletebyid(@RequestBody Map<String, Integer> request) {
        Integer id = request.get("id");
        productService.deleteById(id);
        return Result.success("Product deleted successfully");
    }

    @PostMapping("productchange")
    public Result<Product> productchange(@RequestBody Map<String, Object> request) {
        Product entity = new Product();
        entity.setId((Integer) request.get("id"));
        entity.setName((String) request.get("name"));
        entity.setCategory((String) request.get("category"));
        entity = productService.updateProduct(entity);
        return Result.success("Product updated successfully", entity);
    }

    @PostMapping("productcreate")
    public Result<Product> productcreate(@RequestBody Map<String, Object> request) {
        Product entity = new Product();
        entity.setName((String) request.get("name"));
        entity.setCategory((String) request.get("category"));
        entity.setDescription((String) request.get("description"));
        entity = productService.createProduct(entity);
        return Result.success("Product created successfully", entity);
    }

}