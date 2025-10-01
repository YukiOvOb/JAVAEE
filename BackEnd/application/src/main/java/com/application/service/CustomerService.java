package com.application.service;

import com.application.entity.Product;
import com.application.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.application.dao.*;
import com.application.entity.*;

@Service
public class CustomerService {

    @Autowired
    CustomerDAO customerDAO;

    public Customer createCustomer(Customer customer) {
        return customerDAO.save(customer);
    }

    public Customer getCustomerByName(String name) {
        return customerDAO.findById(name).orElse(null);
    }

    public int updateCustomerPassworde(String name, String password) {
        Customer customer = customerDAO.findById(name).orElse(null);
        if (customer != null) {
            customer.setPassword(password);
            customerDAO.save(customer);
            return 0;
        }
        return 1;
    }

}
