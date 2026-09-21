-- =====================================================================
-- Dữ liệu mẫu để luyện tập (ít, đủ cho mọi bảng)
-- Mật khẩu tất cả tài khoản: 123456  (BCrypt, khớp BCryptPasswordEncoder)
-- UUID cố định, dễ nhớ để test Swagger:
--   users   11111111-...-00x   stores 22222222-...   categories 33333333-...
--   products 66666666-...      variants 77777777-...  orders 88888888-...
-- Chạy lại nhiều lần không lỗi (ON CONFLICT DO NOTHING)
-- =====================================================================
BEGIN;

-- ---------- ROLES ----------
INSERT INTO roles (name, description) VALUES
    ('ADMIN',  'Quản trị hệ thống'),
    ('SELLER', 'Chủ cửa hàng'),
    ('USER',   'Khách hàng')
ON CONFLICT DO NOTHING;

-- ---------- USERS (mật khẩu: 123456) ----------
INSERT INTO users (id, name, email, address, dob, password, status, is_delete) VALUES
    ('11111111-0000-0000-0000-000000000001', 'Quản trị viên', 'admin@shop.com',    'Hà Nội',    '1995-01-15', '$2a$10$wPNTMoKE3pgCE8XD6mkH3OLN.jXPE8S0YcaDhEGDfFufWOxjyVHIy', TRUE, FALSE),
    ('11111111-0000-0000-0000-000000000002', 'Nguyễn Văn Bán', 'seller@shop.com',  'Vinh, Nghệ An', '1998-05-20', '$2a$10$wPNTMoKE3pgCE8XD6mkH3OLN.jXPE8S0YcaDhEGDfFufWOxjyVHIy', TRUE, FALSE),
    ('11111111-0000-0000-0000-000000000003', 'Trần Thị Mua',   'customer@shop.com','TP. Hồ Chí Minh', '2001-09-02', '$2a$10$wPNTMoKE3pgCE8XD6mkH3OLN.jXPE8S0YcaDhEGDfFufWOxjyVHIy', TRUE, FALSE),
    ('11111111-0000-0000-0000-000000000004', 'Lê Văn Khóa',    'locked@shop.com',  'Đà Nẵng',   '1999-12-12', '$2a$10$wPNTMoKE3pgCE8XD6mkH3OLN.jXPE8S0YcaDhEGDfFufWOxjyVHIy', FALSE, FALSE)
ON CONFLICT DO NOTHING;

INSERT INTO user_roles (user_id, role_id)
SELECT u.id, r.id
FROM (VALUES
        ('11111111-0000-0000-0000-000000000001'::uuid, 'ADMIN'),
        ('11111111-0000-0000-0000-000000000002'::uuid, 'SELLER'),
        ('11111111-0000-0000-0000-000000000002'::uuid, 'USER'),
        ('11111111-0000-0000-0000-000000000003'::uuid, 'USER'),
        ('11111111-0000-0000-0000-000000000004'::uuid, 'USER')
     ) AS x(user_id, role_name)
JOIN users u ON u.id = x.user_id
JOIN roles r ON r.name = x.role_name
ON CONFLICT DO NOTHING;

-- ---------- STORES ----------
INSERT INTO stores (id, store_image, store_name, description, owner_id) VALUES
    ('22222222-0000-0000-0000-000000000001', 'https://picsum.photos/seed/store1/600/300', 'Shop Thời Trang Nam', 'Áo, quần nam basic', '11111111-0000-0000-0000-000000000002'),
    ('22222222-0000-0000-0000-000000000002', 'https://picsum.photos/seed/store2/600/300', 'Admin Store',         'Cửa hàng của admin (test quyền)', '11111111-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

INSERT INTO store_followers (id, user_id, store_id) VALUES
    ('22222222-1111-0000-0000-000000000001', '11111111-0000-0000-0000-000000000003', '22222222-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

-- ---------- CATEGORY PRODUCTS (cha - con) ----------
INSERT INTO category_products (id, category_name, parent_id) VALUES
    ('33333333-0000-0000-0000-000000000001', 'Thời trang nam', NULL),
    ('33333333-0000-0000-0000-000000000002', 'Áo nam',  '33333333-0000-0000-0000-000000000001'),
    ('33333333-0000-0000-0000-000000000003', 'Quần nam','33333333-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

-- ---------- COLORS / SIZES (danh mục dùng chung) ----------
INSERT INTO colors (id, color_name, hex_code) VALUES
    ('44444444-0000-0000-0000-000000000001', 'Đen',       '#000000'),
    ('44444444-0000-0000-0000-000000000002', 'Trắng',     '#FFFFFF'),
    ('44444444-0000-0000-0000-000000000003', 'Xanh navy', '#1F2A44')
ON CONFLICT DO NOTHING;

INSERT INTO sizes (id, size_name, sort_order) VALUES
    ('55555555-0000-0000-0000-000000000001', 'S',  1),
    ('55555555-0000-0000-0000-000000000002', 'M',  2),
    ('55555555-0000-0000-0000-000000000003', 'L',  3),
    ('55555555-0000-0000-0000-000000000004', 'XL', 4)
ON CONFLICT DO NOTHING;

-- ---------- PRODUCTS ----------
INSERT INTO products (id, name, image, gender, description, category_product_id, store_id, created_by) VALUES
    ('66666666-0000-0000-0000-000000000001', 'Áo thun basic',   'https://picsum.photos/seed/ao/400/400',   1, 'Cotton 100%, form regular',
     '33333333-0000-0000-0000-000000000002', '22222222-0000-0000-0000-000000000001', 'seller@shop.com'),
    ('66666666-0000-0000-0000-000000000002', 'Quần jean slim',  'https://picsum.photos/seed/quan/400/400', 1, 'Jean co giãn nhẹ',
     '33333333-0000-0000-0000-000000000003', '22222222-0000-0000-0000-000000000001', 'seller@shop.com')
ON CONFLICT DO NOTHING;

-- ---------- PRODUCT VARIANTS (giá + tồn kho) ----------
-- Lấy màu/size theo tên, phòng trường hợp DB đã có sẵn màu/size với id khác
INSERT INTO product_variants (id, product_id, color_id, size_id, sku, price, stock)
SELECT x.id, x.product_id, c.id, s.id, x.sku, x.price, x.stock
FROM (VALUES
        ('77777777-0000-0000-0000-000000000001'::uuid, '66666666-0000-0000-0000-000000000001'::uuid, 'Đen',       'M', 'AO-DEN-M',    199000, 10),
        ('77777777-0000-0000-0000-000000000002'::uuid, '66666666-0000-0000-0000-000000000001'::uuid, 'Đen',       'L', 'AO-DEN-L',    209000,  5),
        ('77777777-0000-0000-0000-000000000003'::uuid, '66666666-0000-0000-0000-000000000001'::uuid, 'Trắng',     'M', 'AO-TRANG-M',  199000, 12),
        ('77777777-0000-0000-0000-000000000004'::uuid, '66666666-0000-0000-0000-000000000001'::uuid, 'Trắng',     'L', 'AO-TRANG-L',  209000,  0),
        ('77777777-0000-0000-0000-000000000005'::uuid, '66666666-0000-0000-0000-000000000002'::uuid, 'Xanh navy', 'M', 'QUAN-NAVY-M', 459000,  7),
        ('77777777-0000-0000-0000-000000000006'::uuid, '66666666-0000-0000-0000-000000000002'::uuid, 'Xanh navy', 'L', 'QUAN-NAVY-L', 459000,  3)
     ) AS x(id, product_id, color_name, size_name, sku, price, stock)
JOIN colors c ON c.color_name = x.color_name
JOIN sizes  s ON s.size_name  = x.size_name
ON CONFLICT DO NOTHING;

-- ---------- USER - PRODUCT ----------
INSERT INTO favorite_products (id, user_id, product_id) VALUES
    ('66666666-1111-0000-0000-000000000001', '11111111-0000-0000-0000-000000000003', '66666666-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

INSERT INTO cart_products (id, user_id, product_variant_id, quantity) VALUES
    ('66666666-2222-0000-0000-000000000001', '11111111-0000-0000-0000-000000000003', '77777777-0000-0000-0000-000000000001', 2)
ON CONFLICT DO NOTHING;

INSERT INTO feedbacks (id, user_id, product_id, comment, star) VALUES
    ('66666666-3333-0000-0000-000000000001', '11111111-0000-0000-0000-000000000003', '66666666-0000-0000-0000-000000000001', 'Áo đẹp, vải mát', 5)
ON CONFLICT DO NOTHING;

-- ---------- ORDER / PAYMENT ----------
INSERT INTO orders (id, user_id, total_price, phone, name, address) VALUES
    ('88888888-0000-0000-0000-000000000001', '11111111-0000-0000-0000-000000000003', 658000, '0901234567', 'Trần Thị Mua', '12 Nguyễn Huệ, Q1, TP. Hồ Chí Minh')
ON CONFLICT DO NOTHING;

INSERT INTO order_details (id, order_id, product_id, product_variant_id, price, quantity) VALUES
    ('88888888-1111-0000-0000-000000000001', '88888888-0000-0000-0000-000000000001', '66666666-0000-0000-0000-000000000001', '77777777-0000-0000-0000-000000000003', 199000, 1),
    ('88888888-1111-0000-0000-000000000002', '88888888-0000-0000-0000-000000000001', '66666666-0000-0000-0000-000000000002', '77777777-0000-0000-0000-000000000006', 459000, 1)
ON CONFLICT DO NOTHING;

INSERT INTO payments (id, type, total_price) VALUES
    ('99999999-0000-0000-0000-000000000001', 'COD',   658000),
    ('99999999-0000-0000-0000-000000000002', 'VNPAY', 0)
ON CONFLICT DO NOTHING;

INSERT INTO transactions (id, user_id, total_price, payment_id, order_id) VALUES
    ('99999999-1111-0000-0000-000000000001', '11111111-0000-0000-0000-000000000003', 658000, '99999999-0000-0000-0000-000000000001', '88888888-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

-- ---------- COMMUNITY ----------
INSERT INTO category_posts (id, category_name) VALUES
    ('aaaaaaaa-0000-0000-0000-000000000001', 'Review sản phẩm'),
    ('aaaaaaaa-0000-0000-0000-000000000002', 'Tin tức')
ON CONFLICT DO NOTHING;

INSERT INTO posts (id, category_post_id, content, created_by, view_count) VALUES
    ('bbbbbbbb-0000-0000-0000-000000000001', 'aaaaaaaa-0000-0000-0000-000000000001', 'Review áo thun basic: mặc mát, giặt không xù.', 'customer@shop.com', 15)
ON CONFLICT DO NOTHING;

INSERT INTO comments (id, user_id, comment, post_id) VALUES
    ('bbbbbbbb-1111-0000-0000-000000000001', '11111111-0000-0000-0000-000000000002', 'Cảm ơn bạn đã ủng hộ shop!', 'bbbbbbbb-0000-0000-0000-000000000001')
ON CONFLICT DO NOTHING;

INSERT INTO reactions (id, post_id, user_id) VALUES
    ('bbbbbbbb-2222-0000-0000-000000000001', 'bbbbbbbb-0000-0000-0000-000000000001', '11111111-0000-0000-0000-000000000002')
ON CONFLICT DO NOTHING;

INSERT INTO notifications (id, user_id, title, content) VALUES
    ('bbbbbbbb-3333-0000-0000-000000000001', '11111111-0000-0000-0000-000000000003', 'Đặt hàng thành công', 'Đơn hàng 658.000đ đang được xử lý'),
    ('bbbbbbbb-3333-0000-0000-000000000002', '11111111-0000-0000-0000-000000000002', 'Có đơn hàng mới',     'Khách Trần Thị Mua vừa đặt 2 sản phẩm')
ON CONFLICT DO NOTHING;

COMMIT;
