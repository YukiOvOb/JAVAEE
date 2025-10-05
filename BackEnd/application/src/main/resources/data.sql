-- ======================================================
-- 1️⃣ ShoppingCart
-- ======================================================
INSERT INTO `shopping_cart` (id) VALUES (1), (2), (3);

-- ======================================================
-- 2️⃣ Customer
-- ======================================================
INSERT INTO `customer` 
(username, first_name, last_name, email, password, address, country, postal_code, phone_number, provider_customer_id, cart_id)
VALUES 
('alice', 'Alice', 'Tan', 'alice@example.com', 'pw123', '123 Orchard Rd', 'Singapore', '238888', '+65-81112222', 'CUST001', 1),
('ben', 'Ben', 'Ng', 'ben@example.com', 'pw123', '88 Serangoon Ave', 'Singapore', '556677', '+65-82223333', 'CUST002', 2),
('charlie', 'Charlie', 'Lim', 'charlie@example.com', 'pw123', '1 Pasir Ris St 1', 'Singapore', '510101', '+65-83334444', 'CUST003', 3);

-- ======================================================
-- 3️⃣ Product
-- ======================================================
INSERT INTO `product` 
(id, name, brand, category, collection, description, image_url, image_alt, stock, unit_price)
VALUES
(1001, 'Wireless Mouse', 'Logi', 'Peripherals', NULL, '2.4GHz ergonomic mouse', 'https://img/mouse', 'Wireless Mouse', 100, 29.90),
(1002, 'Mechanical Keyboard', 'KeyCo', 'Peripherals', NULL, 'Blue switches mechanical keyboard', 'https://img/kb', 'Mechanical Keyboard', 50, 89.00),
(1003, '27-inch Monitor', 'ViewBest', 'Display', NULL, 'Full HD LED monitor', 'https://img/monitor', '27-inch Monitor', 25, 189.00);

-- ======================================================
-- 4️⃣ CartItem
-- ======================================================
INSERT INTO `cart_item` (id, cart_id, product_id, quantity, unit_price, method_type)
VALUES
(2001, 1, 1001, 2, 29.90, 'add'),
(2002, 2, 1002, 1, 89.00, 'add'),
(2003, 3, 1003, 1, 189.00, 'add');

-- ======================================================
-- 5️⃣ Shipment
-- ======================================================
INSERT INTO `shipment` 
(id, courier_name, created_at, delivery_estimate, service_level, shipment_code, shipment_method)
VALUES
(3001, 'SingPost', NOW(), DATE_ADD(NOW(), INTERVAL 3 DAY), 'Standard', 'SHIP001', 'Home Delivery'),
(3002, 'NinjaVan', NOW(), DATE_ADD(NOW(), INTERVAL 1 DAY), 'Express', 'SHIP002', 'Same Day'),
(3003, 'J&T', NOW(), DATE_ADD(NOW(), INTERVAL 5 DAY), 'Economy', 'SHIP003', 'Pickup Point');

-- ======================================================
-- 6️⃣ PaymentMethod
-- ======================================================
INSERT INTO `payment_method`
(id, customer_username, expiry_month, expiry_year, card_type, card_holder_name, last_four_digits, is_default)
VALUES
(4001, 'alice', 12, 2027, 'VISA', 'ALICE TAN', '4242', TRUE),
(4002, 'ben', 11, 2026, 'MASTERCARD', 'BEN NG', '1111', TRUE),
(4003, 'charlie', 6, 2028, 'AMEX', 'CHARLIE LIM', '2222', FALSE);

-- ======================================================
-- 7️⃣ Transaction
-- ======================================================
INSERT INTO `transaction`
(id, charged, created_at, currency, grand_total, idempotency_key, is_default, payment_type, provider, provider_product, provider_transaction_id, payment_method_id)
VALUES
(5001, 'Y', NOW(), 'SGD', 59.80, 'TXN001', TRUE, 'CARD', 'Stripe', 'VISA', 'TXN-1001', 4001),
(5002, 'Y', NOW(), 'SGD', 89.00, 'TXN002', TRUE, 'CARD', 'Stripe', 'MASTERCARD', 'TXN-1002', 4002),
(5003, 'Y', NOW(), 'SGD', 189.00, 'TXN003', TRUE, 'CARD', 'Stripe', 'AMEX', 'TXN-1003', 4003);

-- ======================================================
-- 8️⃣ Order
-- ======================================================
INSERT INTO `order`
(id, created_at, discount_total, fulfilment_status, grand_total, order_status, payment_status, sub_total, tax_total, customer_username, shipment_id, transaction_id)
VALUES
(6001, NOW(), 0, 'Processing', 59.80, 'Pending', 'Paid', 59.80, 0, 'alice', 3001, 5001),
(6002, NOW(), 5, 'Shipped', 84.00, 'Shipped', 'Paid', 89.00, 0, 'ben', 3002, 5002),
(6003, NOW(), 0, 'Delivered', 189.00, 'Completed', 'Paid', 189.00, 0, 'charlie', 3003, 5003);

-- ======================================================
-- 9️⃣ OrderItem
-- ======================================================
INSERT INTO `order_item`
(id, order_id, product_id, product_name, unit_price, item_discount, item_tax, item_total)
VALUES
(7001, 6001, 1001, 'Wireless Mouse', 29.90, 0, 0, 59.80),
(7002, 6002, 1002, 'Mechanical Keyboard', 89.00, 5, 0, 84.00),
(7003, 6003, 1003, '27-inch Monitor', 189.00, 0, 0, 189.00);