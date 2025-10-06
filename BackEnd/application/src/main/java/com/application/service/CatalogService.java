package com.application.service;

import com.application.entity.Product;
import com.application.entity.ProductImage;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class CatalogService {
    private final Map<String, Product> productsByHandle = new LinkedHashMap<>();
    private final Map<String, List<ProductImage>> imagesByHandle = new LinkedHashMap<>();

    @PostConstruct
    public void load() {
        // Get current working directory so relative paths use the project root
        String projectRoot = System.getProperty("user.dir");
        loadProducts(new File(projectRoot, "target/generated-data/products_mysql.csv"));
        loadImages(new File(projectRoot, "target/generated-data/product_images_mysql.csv"));
    }

    public List<Product> listAll() { return new ArrayList<>(productsByHandle.values()); }
    public Optional<Product> findByHandle(String handle) { return Optional.ofNullable(productsByHandle.get(handle)); }
    public List<ProductImage> imagesByHandle(String handle) { return imagesByHandle.getOrDefault(handle, List.of()); }

    private void loadProducts(File f) {
        if (!f.exists()) return;
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
             CSVParser p = new CSVParser(r, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
            for (CSVRecord rec : p) {
                Product prod = new Product();
                prod.setHandle(s(rec, "handle"));
                prod.setName(s(rec, "name"));
                prod.setDescription(s(rec, "description"));
                prod.setImageUrl(s(rec, "image_url"));
                prod.setBrand(s(rec, "brand"));
                prod.setCategory(s(rec, "category"));
                prod.setCollection(s(rec, "collection"));
                prod.setId(parseInt(s(rec, "id")));
                prod.setStock(parseInt(s(rec, "stock")));
                String price = s(rec, "price");
                if (!price.isEmpty()) prod.setPrice(new BigDecimal(price));
                productsByHandle.put(prod.getHandle(), prod);
            }
        } catch (Exception ignore) { }
    }

    private void loadImages(File f) {
        if (!f.exists()) return;
        try (Reader r = new InputStreamReader(new FileInputStream(f), StandardCharsets.UTF_8);
             CSVParser p = new CSVParser(r, CSVFormat.DEFAULT.builder().setHeader().setSkipHeaderRecord(true).build())) {
            for (CSVRecord rec : p) {
                ProductImage img = new ProductImage();
                img.setHandle(s(rec, "handle"));
                img.setPosition(parseInt(s(rec, "position")));
                img.setImageUrl(s(rec, "image_url"));
                img.setAltText(s(rec, "alt_text"));
                imagesByHandle.computeIfAbsent(img.getHandle(), k -> new ArrayList<>()).add(img);
            }
            // Sort images by position
            for (List<ProductImage> list : imagesByHandle.values()) {
                list.sort(Comparator.comparingInt(ProductImage::getPosition));
            }
        } catch (Exception ignore) { }
    }

    private static String s(CSVRecord r, String key) {
        try { String v = r.get(key); return v == null ? "" : v.trim(); } catch (Exception e) { return ""; }
    }
    private static int parseInt(String s) { try { return Integer.parseInt(s.trim()); } catch (Exception e) { return 0; } }
}