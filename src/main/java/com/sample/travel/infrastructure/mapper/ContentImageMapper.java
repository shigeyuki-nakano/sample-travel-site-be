package com.sample.travel.infrastructure.mapper;

import com.sample.travel.infrastructure.entity.ContentImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContentImageMapper {

    /**
     * 全コンテンツ画像をコンテンツIDの昇順・表示順（sort_order）の昇順で取得する。
     *
     * @return コンテンツ画像エンティティのリスト。件数が0件の場合は空リストを返す。
     */
    @Select("""
        SELECT content_id, image_url, sort_order
        FROM t_content_images
        ORDER BY content_id, sort_order
    """)
    List<ContentImageEntity> findAll();
}
