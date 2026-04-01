package com.sample.travel.infrastructure.mapper;

import com.sample.travel.infrastructure.entity.ContentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContentsMapper {

    /**
     * 全コンテンツを都道府県・国・レビュー集計・お気に入り状態と結合してIDの昇順で取得する。
     *
     * @return コンテンツエンティティのリスト。件数が0件の場合は空リストを返す。
     */
    @Select("""
        SELECT
            c.id,
            c.title,
            p.name            AS prefecture_name,
            co.name           AS country_name,
            c.address,
            c.distance,
            c.price,
            c.available_from,
            c.available_to,
            ROUND(AVG(r.rating), 2)                              AS average_rating,
            COUNT(r.id)                                          AS review_count,
            CASE WHEN f.id IS NOT NULL THEN TRUE ELSE FALSE END  AS favorite
        FROM t_contents c
        JOIN  m_prefectures p  ON c.prefecture_id = p.id
        JOIN  m_countries  co  ON p.country_id    = co.id
        LEFT JOIN t_reviews  r ON c.id = r.content_id
        LEFT JOIN t_favorites f ON c.id = f.content_id AND f.user_id = 1
        GROUP BY c.id, c.title, p.name, co.name,
                 c.address, c.distance, c.price,
                 c.available_from, c.available_to, f.id
        ORDER BY c.id
        """)
    List<ContentEntity> findAll();
}
