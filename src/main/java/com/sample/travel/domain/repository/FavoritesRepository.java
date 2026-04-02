package com.sample.travel.domain.repository;

import com.sample.travel.domain.exception.ContentNotFoundException;
import com.sample.travel.domain.exception.FavoriteAlreadyExistsException;
import com.sample.travel.domain.exception.FavoriteNotFoundException;

public interface FavoritesRepository {

    /**
     * お気に入りを登録する。
     *
     * @param userId    ログインユーザーID
     * @param contentId コンテンツID
     * @throws ContentNotFoundException        contentId に対応するコンテンツが存在しない場合
     * @throws FavoriteAlreadyExistsException  既にお気に入り登録済みの場合
     */
    void add(long userId, long contentId);

    /**
     * お気に入りを解除する。
     *
     * @param userId    ログインユーザーID
     * @param contentId コンテンツID
     * @throws ContentNotFoundException  contentId に対応するコンテンツが存在しない場合
     * @throws FavoriteNotFoundException お気に入りが未登録の場合
     */
    void remove(long userId, long contentId);
}
