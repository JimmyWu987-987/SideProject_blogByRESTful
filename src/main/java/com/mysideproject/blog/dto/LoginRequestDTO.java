package com.mysideproject.blog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginRequestDTO {
	
	@NotBlank(message = "帳號不能為空")
	@Size(min = 4, max = 20, message = "帳號長度必須在 4 到 20 個字元")
	private String username; // 會員帳號
	
	@NotBlank(message = "密碼不能為空")
	@Size(min = 6, message = "密碼長度至少 6 個字元")
	private String password; // 會員密碼
}
