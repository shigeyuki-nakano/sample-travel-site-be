package com.sample.travel.infrastructure.mapper;

import com.sample.travel.infrastructure.entity.ContentImageEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ContentImageMapper {

    @Select("""
        SELECT content_id, image_url, sort_order
        FROM t_content_images
        ORDER BY content_id, sort_order
    """)
    List<ContentImageEntity> findAll();
}
