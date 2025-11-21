package com.mysideproject.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JwtResponse {
	
	// 存入 token 訊息
	private String token;
	
	// Token的類型
	private String type = "Bearer";
	
	// 使用者資訊（不含帳密）
	private Integer id;
	private String username;
	
	// (未來可加入：email, roles/權限列表)
}
