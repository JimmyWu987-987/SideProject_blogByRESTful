package com.mysideproject.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysideproject.blog.dto.JwtResponse;
import com.mysideproject.blog.dto.LoginRequestDTO;
import com.mysideproject.blog.dto.RegUserDTO;
import com.mysideproject.blog.exception.DuplicateUsernameException;
import com.mysideproject.blog.jwt.JwtUtils;
import com.mysideproject.blog.model.CustomUserDetails;
import com.mysideproject.blog.service.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
	
	// 負責驗證帳密
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UserService userSvc;
	
	// 負責產生 JWT Token
	@Autowired
	JwtUtils jwtUtils;
	
	
	// 註冊新會員的請求
	@PostMapping("/signup")
	public ResponseEntity<String> registerUser(
			@RequestBody
			@Valid
			RegUserDTO regUserDTO){
		
		try {
			
			userSvc.registerUser(regUserDTO);
			
			// 回傳 201 Created
			return new ResponseEntity<>("會員註冊成功！", HttpStatus.CREATED);
			
		} catch (DuplicateUsernameException e) {
			// 處理 UserService 拋出的 DuplicateUsernameException (例如：帳號重複)
            // 回傳 400 Bad Request
		
			
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	// 登入的請求
	@PostMapping("/signin")
	public ResponseEntity<JwtResponse> authenticateUser(
			@RequestBody
			@Valid
			LoginRequestDTO loginRequest) {
		
		// 1. 創建一個包含使用者帳號和密碼的物件 (使用 Spring Security 的 UsernamePasswordAuthenticationToken)
		
        Authentication authentication = authenticationManager.authenticate(
                // 傳入的物件：需要包含帳號和密碼
                // 帳號：loginRequest.getUsername()
                // 密碼：loginRequest.getPassword()
                // ----------------------------------------------------
                // TODO: 請填入正確的 Spring Security 驗證 Token 類別
                // ----------------------------------------------------
        		// 若失敗，這裡會直接拋出 BadCredentialsException，跳去 GlobalExceptionHandler
        		new UsernamePasswordAuthenticationToken(
        				loginRequest.getUsername(),
        				loginRequest.getPassword()
        				)
        );
        
        // 2. 驗證成功後，將 Authentication 物件放入 SecurityContext 中
        SecurityContextHolder.getContext().setAuthentication(authentication);
        
        // 3. 呼叫 JwtUtils 產生 Token
        String jwt = jwtUtils.generateJwtToken(authentication);
        
        // 4. 從 Authentication 中取出 CustomUserDetails，取得 User ID 和 Username
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        // 5. 構建並回傳包含 JWT 和使用者資訊的標準響應
        return ResponseEntity.ok(new JwtResponse(
        		jwt,
        		"Bearer",
        		userDetails.getUserId(),
        		userDetails.getUsername()
        		// 註：如果未來有權限功能，可以在這裡加入 userDetails.getAuthorities()
        		));
        		
	}
	
	
}
