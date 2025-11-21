package com.mysideproject.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice // 讓這個類別攔截所有 Controller 的錯誤
public class GlobalExceptionHandler {

    // 攔截權限不足的錯誤 (由 PostService 拋出)
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
        // 回傳 403 Forbidden 及自訂的錯誤訊息
        return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
    }
    
    // 攔截自訂的帳號重複錯誤 (由 UserService 拋出)
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<String> handleDuplicateUsernameException(DuplicateUsernameException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // 攔截找不到資源的錯誤 (例如文章 ID 不存在)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    
    // 攔截其他未知的錯誤
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGlobalException(Exception e) {
        e.printStackTrace(); // 在 Console 印出錯誤詳情
        return new ResponseEntity<>("系統發生錯誤: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}