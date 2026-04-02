package com.sample.travel.presentation.handler;

import com.sample.travel.domain.exception.ContentNotFoundException;
import com.sample.travel.domain.exception.FavoriteAlreadyExistsException;
import com.sample.travel.domain.exception.FavoriteNotFoundException;
import com.sample.travel.presentation.dto.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 指定コンテンツが存在しない場合の例外をキャッチして HTTP 404 を返す。
     *
     * @param e キャッチされた例外
     * @return HTTP 404 と ErrorResponse
     */
    @ExceptionHandler(ContentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleContentNotFound(ContentNotFoundException e) {
        ErrorResponse body = new ErrorResponse().type("CONTENT_NOT_FOUND").message(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * お気に入りが未登録の場合の例外をキャッチして HTTP 404 を返す。
     *
     * @param e キャッチされた例外
     * @return HTTP 404 と ErrorResponse
     */
    @ExceptionHandler(FavoriteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleFavoriteNotFound(FavoriteNotFoundException e) {
        ErrorResponse body = new ErrorResponse().type("FAVORITE_NOT_FOUND").message(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(body);
    }

    /**
     * お気に入りが既に登録済みの場合の例外をキャッチして HTTP 409 を返す。
     *
     * @param e キャッチされた例外
     * @return HTTP 409 と ErrorResponse
     */
    @ExceptionHandler(FavoriteAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleFavoriteAlreadyExists(FavoriteAlreadyExistsException e) {
        ErrorResponse body = new ErrorResponse().type("FAVORITE_ALREADY_EXISTS").message(e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * パスパラメータの型変換失敗をキャッチして HTTP 400 を返す。
     *
     * @param e キャッチされた例外
     * @return HTTP 400 と ErrorResponse
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        ErrorResponse body = new ErrorResponse().type("INVALID_PARAMETER").message(e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    /**
     * 予期しない例外をキャッチして HTTP 500 と汎用エラーレスポンスを返す。
     *
     * @param e キャッチされた例外
     * @return HTTP 500 と ErrorResponse
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        ErrorResponse body = new ErrorResponse().type("INTERNAL_SERVER_ERROR").message("Internal Server Error");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
