package com.application.service;

import com.application.entity.Product;
import com.application.entity.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.application.dao.*;

@Service
@Transactional
public class ProductService {

    @Autowired
    ProductDAO productDAO;

    public List<Product> findByCategory(String category) {
        return productDAO.findByCategory(category).orElse(null);
    }

    public Product findById(Integer id) {
        return productDAO.findById(id).orElse(null);
    }

    public List<Product> findByName(String name) {
        return productDAO.findByName(name).orElse(null);
    }
    
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    public void deleteById(Integer id) {
        productDAO.deleteById(id);
    }

    public Product updateProduct(Product product) {
        productDAO.updateProduct(product.getName(), product.getCategory(), product.getId());
        return product;
    }

    public Product createProduct(Product product) {
        return productDAO.save(product);
    }
}
