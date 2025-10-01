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
public interface CustomerDAO extends JpaRepository<Customer, String> {

    @Query("SELECT c FROM Customer c WHERE c.email = ?1")
    Optional <Customer> findByEmail(String email);
    
    @Query("SELECT c FROM Customer c WHERE c.phone = ?1")
    Optional <Customer> findByPhone(String phone);

    @Modifying
    @Transactional
    @Query("Update Customer c set c.phone = ?2, c.address = ?3 , c.email = ?4 where c.name=?1")
    void updateCustomer(String name, String phone, String address, String email);

} 
