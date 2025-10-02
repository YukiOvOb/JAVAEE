package com.application.controller;

import com.application.entity.Product;
import com.application.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {
    private final CatalogService catalog;
    public HomeController(CatalogService catalog) { this.catalog = catalog; }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("products", catalog.listAll());
        return "index";
    }

    @GetMapping("/product/{handle}")
    public String product(@PathVariable String handle, Model model) {
        Product p = catalog.findByHandle(handle).orElse(null);
        if (p == null) return "redirect:/";
        model.addAttribute("product", p);
        model.addAttribute("images", catalog.imagesByHandle(handle));
        return "product";
    }
}