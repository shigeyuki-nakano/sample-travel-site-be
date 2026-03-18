package com.sample.travel.domain.repository;

import com.sample.travel.domain.model.Content;

import java.util.List;

public interface ContentsRepository {

    List<Content> findAll();
}
