package com.sample.travel.domain.exception;

/**
 * 指定された contentId のお気に入りが未登録の場合にスローされる例外（解除時）。
 */
public class FavoriteNotFoundException extends RuntimeException {

    /**
     * @param contentId お気に入りが未登録のコンテンツの ID
     */
    public FavoriteNotFoundException(long contentId) {
        super("Favorite not found for content: " + contentId);
    }
}
