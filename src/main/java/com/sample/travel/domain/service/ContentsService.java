package com.sample.travel.domain.service;

import com.sample.travel.domain.model.Content;
import com.sample.travel.domain.repository.ContentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentsService {

    private final ContentsRepository contentsRepository;

    /**
     * 全コンテンツ（宿泊施設）をIDの昇順で取得する。
     *
     * @return コンテンツのドメインモデルリスト。件数が0件の場合は空リストを返す。
     */
    public List<Content> findAll() {
        return contentsRepository.findAll();
    }
}
