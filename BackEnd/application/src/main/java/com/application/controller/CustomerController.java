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


@RestController
@CrossOrigin
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmailUtil emailUtil;

    @GetMapping("/test")
    public String test() {
        return "CustomerController is working!";
    }

    @PostMapping("/ ")
    public Result<?> createCustomer(@RequestBody Map<String, String> request) {
        try {
            String name = request.get("name");
            String password = request.get("password");
            String email = request.get("email");

            if (name == null || password == null || email == null) {
                return Result.error("Name, password, and email are required");
            }
            
            Customer customer = new Customer();
            customer.setName(name);
            customer.setPassword(password);
            customer.setEmail(email);
            customerService.createCustomer(customer);
            return Result.success("Customer created successfully");
            
        } catch (Exception e) {
            return Result.error("Failed to create customer: " + e.getMessage());
        }
    }
    
    @PostMapping("customerlogin")
    public Result<Customer> customerlogin(@RequestBody Map<String, String> request) {
        String code = request.get("code");
        String name = request.get("name");
        String password = request.get("password");
        Customer entity = customerService.getCustomerByName(name);
        if (entity == null || !entity.getPassword().equals(password)) {
            return Result.error("Invalid name or password");
        }
        return Result.success(entity);
    }

    @PostMapping("customersendemail")
    public String postMethodName(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        Customer entity = customerService.getCustomerByName(name);
        if (entity != null) {
            String email = entity.getEmail();
            String code = emailUtil.generateVerificationCode();
            System.out.println(code);
            emailUtil.sendVerificationCode(email,code);
            return code;
        }
        return "User not found";
    }
    


    @PostMapping("customerchangepassword")
    public Result<?> customerchangepassword(@RequestBody Map<String, String> request) {
        String name = request.get("name");
        String newPassword = request.get("newPassword");

        Customer entity = customerService.getCustomerByName(name);
        if (entity == null) {
            return Result.error("Invalid name");
        }

        entity.setPassword(newPassword);
        customerService.updateCustomerPassworde(name,newPassword);
        return Result.success("Password changed successfully");
    }
    
    




    
    
}
