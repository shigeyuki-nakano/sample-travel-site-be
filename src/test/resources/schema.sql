-- UT用スキーマ定義（本番スキーマと同一構造）

-- ============================================================
-- テーブル命名規則
--   m_ : マスタテーブル（静的な参照データ）
--   t_ : トランザクションテーブル（業務データ）
--   h_ : 履歴テーブル
-- ============================================================

-- m_countries: 国マスタ
CREATE TABLE IF NOT EXISTS m_countries (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id)
);

-- m_prefectures: 都道府県マスタ
CREATE TABLE IF NOT EXISTS m_prefectures (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    country_id BIGINT       NOT NULL,
    name       VARCHAR(100) NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT fk_m_prefectures_country_id FOREIGN KEY (country_id) REFERENCES m_countries (id)
);

CREATE INDEX IF NOT EXISTS idx_m_prefectures_country_id ON m_prefectures (country_id);

-- t_users: ユーザー
CREATE TABLE IF NOT EXISTS t_users (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(255) NOT NULL,
    email      VARCHAR(255) NOT NULL,
    icon_url   TEXT,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT uq_t_users_email UNIQUE (email)
);

-- t_contents: コンテンツ（宿泊施設）
CREATE TABLE IF NOT EXISTS t_contents (
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
    CONSTRAINT fk_t_contents_prefecture_id FOREIGN KEY (prefecture_id) REFERENCES m_prefectures (id)
);

CREATE INDEX IF NOT EXISTS idx_t_contents_prefecture_id ON t_contents (prefecture_id);

-- t_reviews: レビュー
CREATE TABLE IF NOT EXISTS t_reviews (
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
    CONSTRAINT fk_t_reviews_content_id FOREIGN KEY (content_id) REFERENCES t_contents (id),
    CONSTRAINT fk_t_reviews_user_id    FOREIGN KEY (user_id)    REFERENCES t_users    (id)
);

CREATE INDEX IF NOT EXISTS idx_t_reviews_content_id ON t_reviews (content_id);
CREATE INDEX IF NOT EXISTS idx_t_reviews_user_id    ON t_reviews (user_id);

-- t_favorites: お気に入り
CREATE TABLE IF NOT EXISTS t_favorites (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    user_id    BIGINT       NOT NULL,
    content_id BIGINT       NOT NULL,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT uq_t_favorites_user_content UNIQUE (user_id, content_id),
    CONSTRAINT fk_t_favorites_user_id    FOREIGN KEY (user_id)    REFERENCES t_users    (id),
    CONSTRAINT fk_t_favorites_content_id FOREIGN KEY (content_id) REFERENCES t_contents (id)
);

CREATE INDEX IF NOT EXISTS idx_t_favorites_content_id ON t_favorites (content_id);

-- t_content_images: コンテンツ画像
CREATE TABLE IF NOT EXISTS t_content_images (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    content_id BIGINT       NOT NULL,
    image_url  TEXT         NOT NULL,
    sort_order INT          NOT NULL DEFAULT 0,
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL DEFAULT 'system',
    updated_by VARCHAR(100) NOT NULL DEFAULT 'system',
    PRIMARY KEY (id),
    CONSTRAINT fk_t_content_images_content_id FOREIGN KEY (content_id) REFERENCES t_contents (id)
);

CREATE INDEX IF NOT EXISTS idx_t_content_images_content_id ON t_content_images (content_id);
