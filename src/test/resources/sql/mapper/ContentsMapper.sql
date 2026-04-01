-- ContentsMapperSpec 専用テストデータ（トランザクションデータのみ）
-- マスタデータ（m_countries, m_prefectures）は data.sql で定義済み
-- テストケース:
--   - 全件取得（3件）
--   - id 昇順
--   - content_id=1: reviewCount=2, averageRating=4.97
--   - content_id=2: favorite=true（user_id=1 がお気に入り登録）
--   - content_id=1: favorite=false

INSERT INTO t_users (id, name, email) VALUES
    (1, 'テストユーザー', 'test@example.com'),
    (2, 'レビュアー',    'reviewer@example.com');

INSERT INTO t_contents (id, title, prefecture_id, address, distance, price, available_from, available_to) VALUES
    (1, '東京のアパート', 1, '新宿区', '駅から徒歩5分',  15000, '2026-03-20 00:00:00', '2026-03-25 00:00:00'),
    (2, '沖縄のヴィラ',   2, '那覇市', '海岸から徒歩2分', 28000, '2026-04-01 00:00:00', '2026-04-05 00:00:00'),
    (3, 'レビューなしの宿', 1, '渋谷区', '駅から徒歩10分', 10000, '2026-05-01 00:00:00', '2026-05-03 00:00:00');

-- content_id=1: 2件 → avg = (5.00 + 4.94) / 2 = 4.97
-- content_id=2: 1件 → avg = 4.90
-- content_id=3: レビューなし
INSERT INTO t_reviews (content_id, user_id, rating, comment) VALUES
    (1, 2, 5.00, '最高でした'),
    (1, 2, 4.94, 'とても良かったです'),
    (2, 2, 4.90, '素晴らしい眺めでした');

-- user_id=1 が content_id=2 をお気に入り登録
INSERT INTO t_favorites (user_id, content_id) VALUES (1, 2);

INSERT INTO t_content_images (content_id, image_url, sort_order) VALUES
    (1, 'https://example.com/img1.jpg', 1),
    (2, 'https://example.com/img2.jpg', 1);
