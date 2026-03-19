-- UT用スキーマ定義（本番スキーマと同一構造）

CREATE TABLE IF NOT EXISTS countries (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS prefectures (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    country_id BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT fk_prefectures_country_id FOREIGN KEY (country_id) REFERENCES countries (id)
);

CREATE INDEX IF NOT EXISTS idx_prefectures_country_id ON prefectures (country_id);

CREATE TABLE IF NOT EXISTS users (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    icon_url   TEXT,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS contents (
    id             BIGINT       NOT NULL AUTO_INCREMENT,
    title          VARCHAR(255) NOT NULL,
    prefecture_id  BIGINT       NOT NULL,
    address        VARCHAR(255) NOT NULL,
    distance       VARCHAR(255) NOT NULL,
    price          INT          NOT NULL,
    available_from TIMESTAMP    NOT NULL,
    available_to   TIMESTAMP    NOT NULL,
    created_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by     VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by     VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT fk_contents_prefecture_id FOREIGN KEY (prefecture_id) REFERENCES prefectures (id)
);

CREATE INDEX IF NOT EXISTS idx_contents_prefecture_id ON contents (prefecture_id);

CREATE TABLE IF NOT EXISTS reviews (
    id         BIGINT        NOT NULL AUTO_INCREMENT,
    content_id BIGINT        NOT NULL,
    user_id    BIGINT        NOT NULL,
    rating     DECIMAL(3, 2) NOT NULL,
    comment    TEXT          NOT NULL,
    created_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100)  NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100)  NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT fk_reviews_content_id FOREIGN KEY (content_id) REFERENCES contents (id),
    CONSTRAINT fk_reviews_user_id    FOREIGN KEY (user_id)    REFERENCES users    (id)
);

CREATE INDEX IF NOT EXISTS idx_reviews_content_id ON reviews (content_id);
CREATE INDEX IF NOT EXISTS idx_reviews_user_id    ON reviews (user_id);

CREATE TABLE IF NOT EXISTS favorites (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    content_id BIGINT       NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT uq_favorites_user_content UNIQUE (user_id, content_id),
    CONSTRAINT fk_favorites_user_id    FOREIGN KEY (user_id)    REFERENCES users    (id),
    CONSTRAINT fk_favorites_content_id FOREIGN KEY (content_id) REFERENCES contents (id)
);

CREATE INDEX IF NOT EXISTS idx_favorites_content_id ON favorites (content_id);

CREATE TABLE IF NOT EXISTS content_images (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    content_id BIGINT       NOT NULL,
    image_url  TEXT         NOT NULL,
    sort_order INT          NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT fk_content_images_content_id FOREIGN KEY (content_id) REFERENCES contents (id)
);

CREATE INDEX IF NOT EXISTS idx_content_images_content_id ON content_images (content_id);
