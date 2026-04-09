package com.sample.travel.domain.exception;

/**
 * 指定された contentId のお気に入りが既に登録済みの場合にスローされる例外（登録時）。
 */
public class FavoriteAlreadyExistsException extends RuntimeException {

    /**
     * @param contentId 既にお気に入り登録済みのコンテンツの ID
     */
    public FavoriteAlreadyExistsException(long contentId) {
        super("Favorite already exists for content: " + contentId);
    }
}
