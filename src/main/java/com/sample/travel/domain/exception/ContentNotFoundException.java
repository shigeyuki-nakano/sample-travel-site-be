package com.sample.travel.domain.exception;

/**
 * 指定された contentId に対応するコンテンツが存在しない場合にスローされる例外。
 */
public class ContentNotFoundException extends RuntimeException {

    /**
     * @param contentId 存在しないコンテンツの ID
     */
    public ContentNotFoundException(long contentId) {
        super("Content not found: " + contentId);
    }
}
