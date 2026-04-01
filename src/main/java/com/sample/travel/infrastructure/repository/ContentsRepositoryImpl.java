package com.sample.travel.infrastructure.repository;

import com.sample.travel.domain.model.Content;
import com.sample.travel.domain.repository.ContentsRepository;
import com.sample.travel.infrastructure.entity.ContentEntity;
import com.sample.travel.infrastructure.entity.ContentImageEntity;
import com.sample.travel.infrastructure.mapper.ContentImageMapper;
import com.sample.travel.infrastructure.mapper.ContentsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ContentsRepositoryImpl implements ContentsRepository {

    private final ContentsMapper contentsMapper;
    private final ContentImageMapper contentImageMapper;

    /** {@inheritDoc} */
    @Override
    public List<Content> findAll() {
        List<ContentEntity> entities = contentsMapper.findAll();
        Map<Long, List<String>> imagesByContentId = contentImageMapper.findAll().stream()
                .collect(Collectors.groupingBy(
                        ContentImageEntity::getContentId,
                        Collectors.mapping(ContentImageEntity::getImageUrl, Collectors.toList())
                ));

        return entities.stream()
                .map(entity -> entity.toContent(imagesByContentId.getOrDefault(entity.getId(), List.of())))
                .toList();
    }
}
