package com.sample.travel.domain.service;

import com.sample.travel.domain.repository.FavoritesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FavoritesService {

    private final FavoritesRepository favoritesRepository;

    /**
     * お気に入りを登録する。
     *
     * @param userId    ログインユーザーID
     * @param contentId コンテンツID
     */
    public void add(long userId, long contentId) {
        favoritesRepository.add(userId, contentId);
    }

    /**
     * お気に入りを解除する。
     *
     * @param userId    ログインユーザーID
     * @param contentId コンテンツID
     */
    public void remove(long userId, long contentId) {
        favoritesRepository.remove(userId, contentId);
    }
}
