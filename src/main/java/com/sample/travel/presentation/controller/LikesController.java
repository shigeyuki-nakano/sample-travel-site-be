package com.sample.travel.presentation.controller;

import com.sample.travel.domain.service.FavoritesService;
import com.sample.travel.presentation.api.LikesApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikesController implements LikesApi {

    private static final long FIXED_USER_ID = 1L;

    private final FavoritesService favoritesService;

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Void> postLike(Long contentId) {
        favoritesService.add(FIXED_USER_ID, contentId);
        return ResponseEntity.status(201).build();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ResponseEntity<Void> deleteLike(Long contentId) {
        favoritesService.remove(FIXED_USER_ID, contentId);
        return ResponseEntity.noContent().build();
    }
}
