package com.mysideproject.blog.model;

import java.util.Collection;
import java.util.Collections; // 用於返回空的權限列表

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {
	
	private final UserVO userVO; 
	
	// 加入建構子，用於接收 UserVO 實例
	public CustomUserDetails(UserVO userVO) {
        this.userVO = userVO;
    }

	// ----------------------------------------------------
	// 核心方法
	// ----------------------------------------------------
	@Override
	public String getPassword() {
		return userVO.getPassword(); // 返回 UserVO 的密碼
	}

	@Override
	public String getUsername() {
		return userVO.getUsername(); // 返回 UserVO 的帳號
	}
	
	// 可以自訂增加 UserVO 的其他 getter 方法
	// ex: 電話、email 等等
	
	// 會員編號
	public Integer getUserId() {
		return userVO.getUserId();
	}
    
    // ----------------------------------------------------
	// 權限方法
	// ----------------------------------------------------
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// 將資料庫的 role 字串 (e.g., "ROLE_ROOT") 轉為 Spring Security 的權限物件
		return Collections.singletonList(new SimpleGrantedAuthority(userVO.getRole()));
	}
	
	// 讓 Service 層判斷是否為 Root
	public boolean isRoot() {
		return "ROLE_ROOT".equals(userVO.getRole());
	}

	// ----------------------------------------------------
	// 帳號狀態檢查 (必要方法，通常先返回 true)
	// ----------------------------------------------------
	@Override
	public boolean isAccountNonExpired() {
		// 你的 UserVO 沒有過期時間欄位，故先返回 true
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// 你的 UserVO 沒有鎖定狀態欄位，故先返回 true
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// 密碼是否過期檢查，故先返回 true
		return true;
	}

	@Override
	public boolean isEnabled() {
		// 帳號是否啟用檢查，故先返回 true
		return true;
	}
}