package com.mysideproject.blog.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

	// 攔截登入失敗 (帳號不存在或密碼錯誤)
	// BadCredentials: 回傳 401，訊息：「帳號或密碼錯誤」。(外部使用者輸入錯誤)
	// InternalAuthenticationService: 回傳 500，訊息：「系統忙碌中，請稍後再試」。(內部service錯誤)
	// 使用者登入的時候，錯誤(401)是或是內部錯誤(500)，統一由這個例外處理。
	@ExceptionHandler({ BadCredentialsException.class, InternalAuthenticationServiceException.class })
	public ResponseEntity<String> handleAuthException(Exception e) {
		// 為了安全性，通常不會明確說是「帳號錯」還是「密碼錯」，以免被駭客暴力猜測帳號
		return new ResponseEntity<>("帳號或密碼不正確！", HttpStatus.UNAUTHORIZED); // 回傳 401 Unauthorized
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
		return new ResponseEntity<>("系統發生錯誤 " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// 會員註冊相關處理 @Valid 驗證失敗的例外
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {

		Map<String, String> errors = new HashMap<>();

		// 解析錯誤欄位與訊息
		ex.getBindingResult().getFieldErrors().forEach(error -> {
			errors.put(error.getField(), error.getDefaultMessage());
		});

		// 回傳 400 Bad Request 與錯誤 Map
		return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
	}
}