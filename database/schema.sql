-- =====================================================================
-- E-commerce + Community DB  (PostgreSQL 13+; gen_random_uuid() là hàm core)
-- =====================================================================
-- CREATE EXTENSION IF NOT EXISTS pgcrypto;  -- chỉ cần nếu PostgreSQL < 13

-- ---------- USERS ----------
CREATE TABLE users (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name         VARCHAR(255),
    email        VARCHAR(255) UNIQUE,
    address      VARCHAR(255),
    dob          DATE,
    password     VARCHAR(255),
    create_date  DATE    DEFAULT CURRENT_DATE,
    update_date  DATE,
    status       BOOLEAN DEFAULT TRUE,
    is_delete    BOOLEAN DEFAULT FALSE
);

-- ---------- STORE ----------
CREATE TABLE stores (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    store_image  VARCHAR(255),
    store_name   VARCHAR(255),
    description  VARCHAR(255)
);

-- ---------- CATEGORY PRODUCT (self reference) ----------
CREATE TABLE category_products (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_name  VARCHAR(255),
    parent_id      UUID REFERENCES category_products(id) ON DELETE SET NULL
);
CREATE INDEX idx_category_products_parent ON category_products(parent_id);

-- ---------- SIZE / COLOR ----------
CREATE TABLE sizes (
    id         UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    size_name  VARCHAR(255),
    stock      INT DEFAULT 0 CHECK (stock >= 0)
);

CREATE TABLE colors (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    color_name  VARCHAR(255),
    size_id     UUID REFERENCES sizes(id) ON DELETE SET NULL
);
CREATE INDEX idx_colors_size ON colors(size_id);

-- ---------- PRODUCT ----------
CREATE TABLE products (
    id                   UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    image                VARCHAR(255),
    gender               INT,
    description          VARCHAR(255),
    price                NUMERIC(15,2) CHECK (price >= 0),
    total_stock          INT DEFAULT 0 CHECK (total_stock >= 0),
    color_id             UUID REFERENCES colors(id)            ON DELETE SET NULL,
    category_product_id  UUID REFERENCES category_products(id) ON DELETE SET NULL,
    store_id             UUID REFERENCES stores(id)            ON DELETE SET NULL,
    created_date         TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_date         TIMESTAMP,
    created_by           VARCHAR(255)
);
CREATE INDEX idx_products_color    ON products(color_id);
CREATE INDEX idx_products_category ON products(category_product_id);
CREATE INDEX idx_products_store    ON products(store_id);

-- ---------- POST ----------
CREATE TABLE category_posts (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_name  VARCHAR(255)
);

CREATE TABLE posts (
    id                UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    category_post_id  UUID REFERENCES category_posts(id) ON DELETE SET NULL,
    content           TEXT,
    upload_time       TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_by        VARCHAR(255),
    view_count        INT DEFAULT 0
);
CREATE INDEX idx_posts_category ON posts(category_post_id);

-- ---------- USER INTERACTIONS ----------
CREATE TABLE favorite_products (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id     UUID REFERENCES users(id)    ON DELETE CASCADE,
    product_id  UUID REFERENCES products(id) ON DELETE CASCADE,
    CONSTRAINT uk_favorite_user_product UNIQUE (user_id, product_id)
);
CREATE INDEX idx_favorite_product ON favorite_products(product_id);

CREATE TABLE notifications (
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id  UUID REFERENCES users(id) ON DELETE CASCADE,
    title    VARCHAR(255),
    content  VARCHAR(255)
);
CREATE INDEX idx_notifications_user ON notifications(user_id);

CREATE TABLE comments (
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id  UUID REFERENCES users(id) ON DELETE CASCADE,
    comment  VARCHAR(255),
    post_id  UUID REFERENCES posts(id) ON DELETE CASCADE
);
CREATE INDEX idx_comments_user ON comments(user_id);
CREATE INDEX idx_comments_post ON comments(post_id);

CREATE TABLE feedbacks (
    id             UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id        UUID REFERENCES users(id)    ON DELETE SET NULL,
    product_id     UUID REFERENCES products(id) ON DELETE CASCADE,
    comment        VARCHAR(255),
    star           INT CHECK (star BETWEEN 1 AND 5),
    feedback_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_feedbacks_user    ON feedbacks(user_id);
CREATE INDEX idx_feedbacks_product ON feedbacks(product_id);

CREATE TABLE store_followers (
    id        UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id   UUID REFERENCES users(id)  ON DELETE CASCADE,
    store_id  UUID REFERENCES stores(id) ON DELETE CASCADE,
    CONSTRAINT uk_follower_user_store UNIQUE (user_id, store_id)
);
CREATE INDEX idx_store_followers_store ON store_followers(store_id);

CREATE TABLE cart_products (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    product_id  UUID REFERENCES products(id) ON DELETE CASCADE,
    quantity    INT DEFAULT 1 CHECK (quantity > 0),
    user_id     UUID REFERENCES users(id)    ON DELETE CASCADE,
    CONSTRAINT uk_cart_user_product UNIQUE (user_id, product_id)
);
CREATE INDEX idx_cart_products_product ON cart_products(product_id);

CREATE TABLE reactions (
    id       UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    post_id  UUID REFERENCES posts(id) ON DELETE CASCADE,
    user_id  UUID REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uk_reaction_user_post UNIQUE (user_id, post_id)
);
CREATE INDEX idx_reactions_post ON reactions(post_id);

-- ---------- ORDER / PAYMENT ----------
CREATE TABLE orders (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id      UUID REFERENCES users(id) ON DELETE SET NULL,
    total_price  NUMERIC(15,2) CHECK (total_price >= 0),
    phone        VARCHAR(255),
    name         VARCHAR(255),
    address      VARCHAR(255)
);
CREATE INDEX idx_orders_user ON orders(user_id);

CREATE TABLE order_details (
    id          UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    order_id    UUID REFERENCES orders(id)   ON DELETE CASCADE,
    product_id  UUID REFERENCES products(id) ON DELETE SET NULL,
    price       NUMERIC(15,2) CHECK (price >= 0),
    quantity    INT CHECK (quantity > 0)
);
CREATE INDEX idx_order_details_order   ON order_details(order_id);
CREATE INDEX idx_order_details_product ON order_details(product_id);

CREATE TABLE payments (
    id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    type         VARCHAR(255),
    total_price  NUMERIC(15,2) CHECK (total_price >= 0)
);

CREATE TABLE transactions (
    id            UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id       UUID REFERENCES users(id)    ON DELETE SET NULL,
    total_price   NUMERIC(15,2) CHECK (total_price >= 0),
    payment_id    UUID REFERENCES payments(id) ON DELETE SET NULL,
    order_id      UUID REFERENCES orders(id)   ON DELETE SET NULL,
    payment_time  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX idx_transactions_user    ON transactions(user_id);
CREATE INDEX idx_transactions_payment ON transactions(payment_id);
CREATE INDEX idx_transactions_order   ON transactions(order_id);
CREATE TABLE roles (
                       id           UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       name         VARCHAR(50) NOT NULL UNIQUE,
                       description  VARCHAR(255)
);

-- Bảng nối user - role (N-N)
CREATE TABLE user_roles (
                            user_id  UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                            role_id  UUID NOT NULL REFERENCES roles(id) ON DELETE CASCADE,
                            PRIMARY KEY (user_id, role_id)
);
CREATE INDEX idx_user_roles_role ON user_roles(role_id);

-- Thêm chủ cửa hàng cho store
ALTER TABLE stores ADD COLUMN owner_id UUID REFERENCES users(id) ON DELETE SET NULL;
CREATE INDEX idx_stores_owner ON stores(owner_id);

