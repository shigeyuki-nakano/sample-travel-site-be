package com.sample.travel.infrastructure.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface FavoritesMapper {

    /**
     * 指定 contentId のコンテンツ件数を返す。
     * 戻り値 &gt; 0 でコンテンツが存在する。
     *
     * @param contentId コンテンツID
     * @return コンテンツの件数
     */
    @Select("""
            SELECT COUNT(1) FROM t_contents WHERE id = #{contentId}
            """)
    int countByContentId(long contentId);

    /**
     * 指定 userId × contentId のお気に入り件数を返す。
     * 戻り値 &gt; 0 でお気に入りが存在する。
     *
     * @param userId    ユーザーID
     * @param contentId コンテンツID
     * @return お気に入りの件数
     */
    @Select("""
            SELECT COUNT(1) FROM t_favorites
            WHERE user_id = #{userId} AND content_id = #{contentId}
            """)
    int countByUserIdAndContentId(long userId, long contentId);

    /**
     * お気に入りを登録する。
     *
     * @param userId    ユーザーID
     * @param contentId コンテンツID
     */
    @Insert("""
            INSERT INTO t_favorites (user_id, content_id)
            VALUES (#{userId}, #{contentId})
            """)
    void insert(long userId, long contentId);

    /**
     * お気に入りを削除する。
     *
     * @param userId    ユーザーID
     * @param contentId コンテンツID
     */
    @Delete("""
            DELETE FROM t_favorites
            WHERE user_id = #{userId} AND content_id = #{contentId}
            """)
    void deleteByUserIdAndContentId(long userId, long contentId);
}
