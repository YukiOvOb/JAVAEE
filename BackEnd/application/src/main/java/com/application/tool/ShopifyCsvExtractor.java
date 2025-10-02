// 

package com.application.tool;

import org.apache.commons.csv.*;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*
This class extracts product and image data from Shopify CSV files in src/main/resources/data.
It groups rows by product handle, normalizes core fields, and orders images by position.
It writes two combined CSV files into target/generated-data for backend consumption.
Run the main method to generate these files before CatalogService loads them.
*/
public class ShopifyCsvExtractor {

    static class Img {
        String url, alt; int pos = Integer.MAX_VALUE;
        Img(String url, String alt, int pos){ this.url=url; this.alt=alt; this.pos=pos; }
    }
    static class P {
        String handle, title, desc, brand, category, cover;
        String price = "0";
        int stockSum = 0;
        Map<String, Img> images = new LinkedHashMap<>();
        int bestPos = Integer.MAX_VALUE;
    }
    public static class ProductRow {
        public String handle, title, description, imageUrl, price, brand, category, collection;
        public int stock;
    }
    public static class ProductImageRow {
        public String handle; public int position; public String imageUrl; public String altText;
    }
    public static class Result {
        public List<ProductRow> products = new ArrayList<>();
        public List<ProductImageRow> images = new ArrayList<>();
    }

    public static Result extract(String resourcePath, String collection) throws Exception {
        File in = new ClassPathResource(resourcePath).getFile();
        Map<String, P> map = new LinkedHashMap<>();
        try (Reader r = new InputStreamReader(new FileInputStream(in), StandardCharsets.UTF_8);
             CSVParser parser = new CSVParser(r, CSVFormat.DEFAULT.builder()
                     .setHeader().setSkipHeaderRecord(true).setIgnoreEmptyLines(true).build())) {
            for (CSVRecord rec : parser) {
                String handle = get(rec,"Handle");
                if (handle.isEmpty()) continue;
                P p = map.computeIfAbsent(handle, k -> { P x = new P(); x.handle=k; return x; });

                if (isEmpty(p.title))    p.title    = get(rec,"Title");
                if (isEmpty(p.desc))     p.desc     = get(rec,"Body (HTML)");
                if (isEmpty(p.brand))    p.brand    = get(rec,"Vendor");
                if (isEmpty(p.category)) p.category = get(rec,"Type");

                String price = get(rec,"Variant Price");
                if (!price.isEmpty() && ("0".equals(p.price) || isEmpty(p.price))) {
                    p.price = price.replace(",", ".");
                }
                String qty = get(rec,"Variant Inventory Qty");
                if (!qty.isEmpty()) { try { p.stockSum += Integer.parseInt(qty.trim()); } catch (Exception ignore) {} }

                String url = get(rec,"Image Src");
                if (!url.isEmpty()) {
                    int pos = parseIntOrMax(get(rec,"Image Position"));
                    String alt = get(rec,"Image Alt Text");
                    Img old = p.images.get(url);
                    if (old == null || pos < old.pos) p.images.put(url, new Img(url, alt, pos));
                    if (pos < p.bestPos) { p.bestPos = pos; p.cover = url; }
                }
            }
        }
        Result out = new Result();
        for (P p : map.values()) {
            ProductRow pr = new ProductRow();
            pr.handle = nz(p.handle); pr.title = nz(p.title); pr.description = nz(p.desc);
            pr.imageUrl = nz(p.cover); pr.price = nz(p.price); pr.brand = nz(p.brand);
            pr.category = nz(p.category); pr.stock = p.stockSum; pr.collection = collection;
            out.products.add(pr);

            List<Img> imgs = new ArrayList<>(p.images.values());
            imgs.sort(Comparator.comparingInt(i -> i.pos));
            int idx = 1;
            for (Img img : imgs) {
                int pos = (img.pos == Integer.MAX_VALUE) ? idx : img.pos;
                ProductImageRow ir = new ProductImageRow();
                ir.handle = pr.handle; ir.position = pos; ir.imageUrl = nz(img.url); ir.altText = nz(img.alt);
                out.images.add(ir);
                idx++;
            }
        }
        return out;
    }

    static String get(CSVRecord r, String key){
        try { if (r.isMapped(key)) { String v = r.get(key); return v==null? "": v.trim(); } }
        catch (Exception ignore) {}
        return "";
    }
    static boolean isEmpty(String s){ return s==null || s.isEmpty(); }
    static String nz(String s){ return s==null? "": s; }
    static int parseIntOrMax(String s){ try { return Integer.parseInt(s.trim()); } catch (Exception e){ return Integer.MAX_VALUE; } }

    public static void main(String[] args) throws Exception {
        Result jewelery = extract("data/jewelery.csv", "jewelery");
        Result apparel = extract("data/apparel.csv", "apparel");
        Result home = extract("data/home-and-garden.csv", "home-and-garden");

        List<ProductRow> products = new ArrayList<>();
        products.addAll(jewelery.products);
        products.addAll(apparel.products);
        products.addAll(home.products);

        List<ProductImageRow> images = new ArrayList<>();
        images.addAll(jewelery.images);
        images.addAll(apparel.images);
        images.addAll(home.images);

        writeCombinedCSVs(products, images);
    }

    private static void writeCombinedCSVs(List<ProductRow> products, List<ProductImageRow> images) throws Exception {
        File outDir = new File("target/generated-data");
        if (!outDir.exists()) outDir.mkdirs();

        File productsFile = new File(outDir, "products_mysql.csv");
        try (Writer w = new OutputStreamWriter(new FileOutputStream(productsFile), StandardCharsets.UTF_8);
             CSVPrinter pr = new CSVPrinter(w, CSVFormat.DEFAULT.builder()
                     .setHeader("handle","title","description","image_url","price","brand","category","stock")
                     .build())) {
            for (ProductRow p : products) {
                pr.printRecord(nz(p.handle), nz(p.title), nz(p.description),
                        nz(p.imageUrl), nz(p.price), nz(p.brand), nz(p.category), p.stock);
            }
        }

        File imagesFile = new File(outDir, "product_images_mysql.csv");
        try (Writer w = new OutputStreamWriter(new FileOutputStream(imagesFile), StandardCharsets.UTF_8);
             CSVPrinter pr = new CSVPrinter(w, CSVFormat.DEFAULT.builder()
                     .setHeader("handle","position","image_url","alt_text")
                     .build())) {
            for (ProductImageRow i : images) {
                pr.printRecord(nz(i.handle), i.position, nz(i.imageUrl), nz(i.altText));
            }
        }
        System.out.println("OK. Written: " + productsFile.getAbsolutePath() + " and " + imagesFile.getAbsolutePath());
    }
}
