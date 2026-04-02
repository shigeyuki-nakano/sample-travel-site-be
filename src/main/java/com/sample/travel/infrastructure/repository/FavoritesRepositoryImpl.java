package com.sample.travel.infrastructure.repository;

import com.sample.travel.domain.exception.ContentNotFoundException;
import com.sample.travel.domain.exception.FavoriteAlreadyExistsException;
import com.sample.travel.domain.exception.FavoriteNotFoundException;
import com.sample.travel.domain.repository.FavoritesRepository;
import com.sample.travel.infrastructure.mapper.FavoritesMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FavoritesRepositoryImpl implements FavoritesRepository {

    private final FavoritesMapper favoritesMapper;

    /** {@inheritDoc} */
    @Override
    public void add(long userId, long contentId) {
        if (favoritesMapper.countByContentId(contentId) == 0) {
            throw new ContentNotFoundException(contentId);
        }
        if (favoritesMapper.countByUserIdAndContentId(userId, contentId) > 0) {
            throw new FavoriteAlreadyExistsException(contentId);
        }
        favoritesMapper.insert(userId, contentId);
    }

    /** {@inheritDoc} */
    @Override
    public void remove(long userId, long contentId) {
        if (favoritesMapper.countByContentId(contentId) == 0) {
            throw new ContentNotFoundException(contentId);
        }
        if (favoritesMapper.countByUserIdAndContentId(userId, contentId) == 0) {
            throw new FavoriteNotFoundException(contentId);
        }
        favoritesMapper.deleteByUserIdAndContentId(userId, contentId);
    }
}
