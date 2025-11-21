package com.mysideproject.blog.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class RegUserDTO {
	private String username; // 會員帳號
	private String password; // 會員密碼
	private String name; // 會員姓名
	private String email; // 會員Email
	private String mobile; // 會員電話
	private LocalDate birthday; // 生日
}
