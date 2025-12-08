package com.mysideproject.blog.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class RegUserDTO {
	
	@NotBlank(message = "帳號不能為空")
	@Size(min = 4, max = 20, message = "帳號長度必須在 4 到 20 個字元")
	private String username; // 會員帳號
	
	@NotBlank(message = "密碼不能為空")
	@Size(min = 6, message = "密碼長度至少 6 個字元")
	private String password; // 會員密碼
	
	@NotBlank(message = "姓名不能為空")
	private String name; // 會員姓名
	
	@NotBlank(message = "Email不能為空")
	@Email(message = "Email 格式不正確")
	private String email; // 會員Email
	
	@NotBlank(message = "電話不能為空")
	@Pattern(regexp = "^09[0-9]{2}-[0-9]{6}$", message = "手機格式不符，範例: 0912-123456")
	private String mobile; // 會員電話
	
	@NotNull(message = "生日不能為空")
	@Past(message = "生日必須為過去的生日")
	private LocalDate birthday; // 生日
	
}
