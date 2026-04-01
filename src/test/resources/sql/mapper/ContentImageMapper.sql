-- ContentImageMapperSpec 専用テストデータ（トランザクションデータのみ）
-- マスタデータ（m_countries, m_prefectures）は data.sql で定義済み
-- テストケース:
--   - 全件取得（3件）
--   - sort_order 昇順
--   - content_id=1 の imageUrl 検証

INSERT INTO t_users (id, name, email) VALUES
    (1, 'テストユーザー', 'test@example.com');

INSERT INTO t_contents (id, title, prefecture_id, address, distance, price, available_from, available_to) VALUES
    (1, '東京のアパート', 1, '新宿区', '駅から徒歩5分',  15000, '2026-03-20 00:00:00', '2026-03-25 00:00:00'),
    (2, '沖縄のヴィラ',   2, '那覇市', '海岸から徒歩2分', 28000, '2026-04-01 00:00:00', '2026-04-05 00:00:00');

-- content_id=1: 2件（sort_order=1,2）、content_id=2: 1件
INSERT INTO t_content_images (content_id, image_url, sort_order) VALUES
    (1, 'https://example.com/img1a.jpg', 1),
    (1, 'https://example.com/img1b.jpg', 2),
    (2, 'https://example.com/img2.jpg',  1);
