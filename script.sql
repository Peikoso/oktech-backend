-- ==========================================
-- USERS
-- ==========================================
INSERT INTO users (id, name, email, cpf, password, auth_provider, provider_id, role, phone)
VALUES 
(gen_random_uuid(), 'João Silva', 'joao.silva@example.com', '111.222.333-44', 'hashed_password_1', 'LOCAL', NULL, 'USER', '(71) 98888-1111'),
(gen_random_uuid(), 'Maria Souza', 'maria.souza@example.com', '222.333.444-55', 'hashed_password_2', 'LOCAL', NULL, 'PRODUCTOR', '(71) 97777-2222'),
(gen_random_uuid(), 'Carlos Pereira', 'carlos.pereira@example.com', '333.444.555-66', 'hashed_password_3', 'GOOGLE', 'google-12345', 'ADMIN', '(71) 96666-3333');

-- ==========================================
-- SHOPS
-- ==========================================
INSERT INTO shops (id, name, description, cnpj, owner_id)
SELECT gen_random_uuid(), 'Mercadinho Central', 'Seu mercado de bairro.', '12.345.678/0001-90', u.id
FROM users u WHERE u.role = 'PRODUCTOR' LIMIT 1;

INSERT INTO shops (id, name, description, cnpj, owner_id)
SELECT gen_random_uuid(), 'Loja Tech', 'Eletrônicos e acessórios.', '98.765.432/0001-21', u.id
FROM users u WHERE u.role = 'PRODUCTOR' LIMIT 1 OFFSET 0;

-- ==========================================
-- ADDRESS
-- ==========================================
INSERT INTO address (id, user_id, street, city, state, complement, cep)
SELECT gen_random_uuid(), id, 'Rua das Flores, 123', 'Salvador', 'BA', 'Apto 202', '40000-000' FROM users LIMIT 1;

INSERT INTO address (id, user_id, street, city, state, complement, cep)
SELECT gen_random_uuid(), id, 'Av. Principal, 500', 'Salvador', 'BA', NULL, '40000-111' FROM users OFFSET 1 LIMIT 1;

-- ==========================================
-- PRODUCTS
-- ==========================================
INSERT INTO products (id, shop_id, name, description, price, category, stock)
SELECT gen_random_uuid(), s.id, 'Arroz 5kg', 'Arroz tipo 1', 2500, 'Alimentos', 50
FROM shops s LIMIT 1;

INSERT INTO products (id, shop_id, name, description, price, category, stock)
SELECT gen_random_uuid(), s.id, 'Notebook Gamer', 'Notebook com 16GB RAM e RTX 3060', 550000, 'Eletrônicos', 10
FROM shops s OFFSET 1 LIMIT 1;

INSERT INTO products (id, shop_id, name, description, price, category, stock)
SELECT gen_random_uuid(), s.id, 'Feijão 1kg', 'Feijão carioca selecionado', 800, 'Alimentos', 30
FROM shops s LIMIT 1;

-- ==========================================
-- PRODUCT IMAGES
-- ==========================================
INSERT INTO product_images (id, image_url, product_id)
SELECT gen_random_uuid(), 'https://picsum.photos/seed/arroz/300/300', p.id
FROM products p WHERE p.name = 'Arroz 5kg';

INSERT INTO product_images (id, image_url, product_id)
SELECT gen_random_uuid(), 'https://picsum.photos/seed/notebook/300/300', p.id
FROM products p WHERE p.name = 'Notebook Gamer';

-- ==========================================
-- ORDERS
-- ==========================================
INSERT INTO orders (id, user_id, status)
SELECT gen_random_uuid(), u.id, 'PENDING'
FROM users u LIMIT 1;

-- ==========================================
-- ORDER ITEMS
-- ==========================================
INSERT INTO order_items (id, order_id, product_id, quantity, delivery_status, address_id)
SELECT 
    gen_random_uuid(),
    o.id,
    p.id,
    2,
    'PENDING',
    a.id
FROM orders o
JOIN products p ON p.name = 'Arroz 5kg'
JOIN address a ON a.user_id = o.user_id;

INSERT INTO order_items (id, order_id, product_id, quantity, delivery_status, address_id)
SELECT 
    gen_random_uuid(),
    o.id,
    p.id,
    1,
    'PENDING',
    a.id
FROM orders o
JOIN products p ON p.name = 'Feijão 1kg'
JOIN address a ON a.user_id = o.user_id;
