-- FavoritesMapperSpec 専用テストデータ（トランザクションデータのみ）
-- マスタデータ（m_countries, m_prefectures）は data.sql で定義済み
-- テストケース:
--   - content_id=1: 存在するコンテンツ
--   - content_id=2: 存在するコンテンツ（user_id=1 のお気に入り未登録）
--   - user_id=1 が content_id=1 をお気に入り登録済み

INSERT INTO t_users (id, name, email) VALUES
    (1, 'テストユーザー', 'test@example.com');

INSERT INTO t_contents (id, title, prefecture_id, address, distance, price, available_from, available_to) VALUES
    (1, '東京のアパート', 1, '新宿区', '駅から徒歩5分', 15000, '2026-03-20 00:00:00', '2026-03-25 00:00:00'),
    (2, '沖縄のヴィラ',   2, '那覇市', '海岸から徒歩2分', 28000, '2026-04-01 00:00:00', '2026-04-05 00:00:00');

-- user_id=1 が content_id=1 をお気に入り登録済み（content_id=2 は未登録）
INSERT INTO t_favorites (user_id, content_id) VALUES (1, 1);
