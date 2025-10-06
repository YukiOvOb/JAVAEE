package com.application.controller;

import com.application.entity.Product;
import com.application.service.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@Controller
@SessionAttributes("cart")
public class CartController {
    private final CatalogService catalog;
    public CartController(CatalogService catalog) { this.catalog = catalog; }

    @ModelAttribute("cart")
    public Map<String, Integer> cart() { return new LinkedHashMap<>(); }

    @PostMapping("/cart/add/{handle}")
    public String add(@PathVariable String handle, @RequestParam(defaultValue = "1") int qty,
                      @ModelAttribute("cart") Map<String, Integer> cart) {
        cart.merge(handle, Math.max(1, qty), Integer::sum);
        return "redirect:/cart";
    }

    @PostMapping("/cart/update/{handle}")
    public String update(@PathVariable String handle, @RequestParam int qty,
                         @ModelAttribute("cart") Map<String, Integer> cart) {
        if (qty <= 0) cart.remove(handle); else cart.put(handle, qty);
        return "redirect:/cart";
    }

    @PostMapping("/cart/remove/{handle}")
    public String remove(@PathVariable String handle, @ModelAttribute("cart") Map<String, Integer> cart) {
        cart.remove(handle);
        return "redirect:/cart";
    }

    @GetMapping("/cart")
    public String view(@ModelAttribute("cart") Map<String, Integer> cart, Model model) {
        List<ItemView> items = new ArrayList<>();
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> e : cart.entrySet()) {
            Product p = catalog.findByHandle(e.getKey()).orElse(null);
            if (p == null) continue;
            BigDecimal subtotal = p.getPrice() == null ? BigDecimal.ZERO : p.getPrice().multiply(BigDecimal.valueOf(e.getValue()));
            total = total.add(subtotal);
            items.add(new ItemView(p.getHandle(), p.getName(), p.getImageUrl(), p.getPrice(), e.getValue(), subtotal));
        }
        model.addAttribute("items", items);
        model.addAttribute("total", total);
        return "cart";
    }

    public record ItemView(String handle, String title, String imageUrl, BigDecimal price, int qty, BigDecimal subtotal) {}
}