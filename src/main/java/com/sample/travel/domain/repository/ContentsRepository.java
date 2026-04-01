package com.sample.travel.domain.repository;

import com.sample.travel.domain.model.Content;

import java.util.List;

public interface ContentsRepository {

    /**
     * 全コンテンツ（宿泊施設）をIDの昇順で取得する。
     *
     * @return コンテンツのドメインモデルリスト。件数が0件の場合は空リストを返す。
     */
    List<Content> findAll();
}
