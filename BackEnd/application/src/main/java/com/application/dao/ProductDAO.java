package com.application.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.application.entity.*;

import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductDAO extends JpaRepository<Product, Integer> {

    @Query("SELECT p FROM Product p WHERE p.category = ?1")
    Optional <List<Product>> findByCategory(String category);

    @Query("SELECT p FROM Product p WHERE p.name = ?1")
    Optional<List<Product>> findByName(String name);

    @Modifying
    @Transactional
    @Query("Update Product p set p.name = ?1 , p.category = ?2 where p.id = ?3")
    void updateProduct(String name, String category, Integer id);
}   