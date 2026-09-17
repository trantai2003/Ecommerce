-- =====================================================================
-- Migration: cập nhật DB đang chạy sang cấu trúc mới (giữ dữ liệu)
--   * colors, sizes  -> bảng danh mục độc lập (không trỏ tới nhau, không trỏ tới products)
--   * product_variants -> giá + tồn kho theo product + color + size
--   * cart_products, order_details -> trỏ tới product_variants
--   * products: bỏ price, total_stock; thêm name
-- Chạy được cho CẢ 2 trạng thái DB:
--   (A) schema gốc:          products.color_id -> colors.size_id -> sizes
--   (B) đã chạy script trước: colors.product_id, sizes.color_id
-- Kết quả khớp với schema.sql mới. Chạy cả file 1 lần (không chạy từng câu).
-- =====================================================================
BEGIN;

-- 1. Bảng biến thể
CREATE TABLE product_variants (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id    UUID NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    color_id      UUID NOT NULL REFERENCES colors(id)   ON DELETE RESTRICT,
    size_id       UUID NOT NULL REFERENCES sizes(id)    ON DELETE RESTRICT,
    sku           VARCHAR(100) UNIQUE,
    price         NUMERIC(15,2) NOT NULL CHECK (price >= 0),
    stock         INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    created_date  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date  TIMESTAMP,
    CONSTRAINT uk_variant_product_color_size UNIQUE (product_id, color_id, size_id)
);
CREATE INDEX idx_variants_color ON product_variants(color_id);
CREATE INDEX idx_variants_size  ON product_variants(size_id);

-- 2. Đọc tổ hợp (product, color, size, stock) từ cấu trúc cũ, tùy DB đang ở trạng thái A hay B
CREATE TEMP TABLE legacy_variant (
    product_id UUID, color_id UUID, size_id UUID, stock INT
) ON COMMIT DROP;

DO $$
BEGIN
    IF EXISTS (SELECT 1 FROM information_schema.columns
               WHERE table_schema = 'public' AND table_name = 'products' AND column_name = 'color_id') THEN
        -- (A) schema gốc
        EXECUTE $q$
            INSERT INTO legacy_variant
            SELECT p.id, c.id, s.id, s.stock
            FROM products p
            JOIN colors c ON c.id = p.color_id
            JOIN sizes  s ON s.id = c.size_id
        $q$;
    ELSIF EXISTS (SELECT 1 FROM information_schema.columns
                  WHERE table_schema = 'public' AND table_name = 'colors' AND column_name = 'product_id') THEN
        -- (B) đã chạy script lần trước
        EXECUTE $q$
            INSERT INTO legacy_variant
            SELECT c.product_id, c.id, s.id, s.stock
            FROM sizes s
            JOIN colors c ON c.id = s.color_id
            WHERE c.product_id IS NOT NULL
        $q$;
    ELSE
        RAISE EXCEPTION 'Không nhận ra cấu trúc colors/sizes hiện tại - dừng migration';
    END IF;
END $$;

-- 3. Gom màu/size trùng tên (không phân biệt hoa thường, khoảng trắng) về 1 id
CREATE TEMP TABLE color_map ON COMMIT DROP AS
SELECT id AS old_id,
       FIRST_VALUE(id) OVER (PARTITION BY lower(trim(color_name)) ORDER BY id) AS new_id
FROM colors;

CREATE TEMP TABLE size_map ON COMMIT DROP AS
SELECT id AS old_id,
       FIRST_VALUE(id) OVER (PARTITION BY lower(trim(size_name)) ORDER BY id) AS new_id
FROM sizes;

-- 4. Đổ dữ liệu vào product_variants (giá lấy từ products.price)
INSERT INTO product_variants (product_id, color_id, size_id, price, stock)
SELECT lv.product_id, cm.new_id, sm.new_id, COALESCE(p.price, 0), COALESCE(lv.stock, 0)
FROM legacy_variant lv
JOIN color_map cm ON cm.old_id = lv.color_id
JOIN size_map  sm ON sm.old_id = lv.size_id
JOIN products  p  ON p.id = lv.product_id
ON CONFLICT (product_id, color_id, size_id)
DO UPDATE SET stock = product_variants.stock + EXCLUDED.stock;

-- 5. Xóa liên kết cũ (xóa cột thì FK, index, unique trên cột đó cũng bị xóa theo)
ALTER TABLE products DROP COLUMN IF EXISTS color_id;
ALTER TABLE colors   DROP COLUMN IF EXISTS size_id,  DROP COLUMN IF EXISTS product_id;
ALTER TABLE sizes    DROP COLUMN IF EXISTS color_id, DROP COLUMN IF EXISTS stock;

-- 6. Dọn màu/size trùng, chuẩn hóa tên, thêm cột + unique
DELETE FROM sizes  WHERE id NOT IN (SELECT new_id FROM size_map);
DELETE FROM colors WHERE id NOT IN (SELECT new_id FROM color_map);

UPDATE colors SET color_name = trim(color_name)
WHERE  color_name IS DISTINCT FROM trim(color_name);
UPDATE sizes  SET size_name = upper(trim(size_name))          -- m -> M
WHERE  size_name IS DISTINCT FROM upper(trim(size_name));

ALTER TABLE colors ADD COLUMN hex_code   VARCHAR(7);
ALTER TABLE sizes  ADD COLUMN sort_order INT DEFAULT 0;
ALTER TABLE colors ADD CONSTRAINT uk_color_name UNIQUE (color_name);
ALTER TABLE sizes  ADD CONSTRAINT uk_size_name  UNIQUE (size_name);

-- 7. Giỏ hàng + chi tiết đơn hàng trỏ tới biến thể
ALTER TABLE cart_products
    ADD COLUMN product_variant_id UUID REFERENCES product_variants(id) ON DELETE CASCADE;
ALTER TABLE order_details
    ADD COLUMN product_variant_id UUID REFERENCES product_variants(id) ON DELETE SET NULL;

-- Sản phẩm có đúng 1 biến thể thì tự gán
CREATE TEMP TABLE single_variant ON COMMIT DROP AS
SELECT product_id, (array_agg(id))[1] AS variant_id
FROM product_variants
GROUP BY product_id
HAVING count(*) = 1;

UPDATE cart_products cp
SET    product_variant_id = sv.variant_id
FROM   single_variant sv
WHERE  sv.product_id = cp.product_id;

UPDATE order_details od
SET    product_variant_id = sv.variant_id
FROM   single_variant sv
WHERE  sv.product_id = od.product_id;

-- Giỏ hàng không xác định được màu/size thì xóa (khách chọn lại);
-- order_details giữ nguyên product_id để không mất lịch sử đơn
DELETE FROM cart_products WHERE product_variant_id IS NULL;

ALTER TABLE cart_products DROP COLUMN product_id;              -- xóa luôn uk_cart_user_product
ALTER TABLE cart_products ADD CONSTRAINT uk_cart_user_variant UNIQUE (user_id, product_variant_id);
CREATE INDEX idx_cart_products_variant ON cart_products(product_variant_id);
CREATE INDEX idx_order_details_variant ON order_details(product_variant_id);

-- 8. products: giá + tồn kho đã chuyển sang biến thể; thêm tên sản phẩm
ALTER TABLE products DROP COLUMN price, DROP COLUMN total_stock;
ALTER TABLE products ADD COLUMN name VARCHAR(255);

COMMIT;
