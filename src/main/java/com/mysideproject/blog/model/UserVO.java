package com.mysideproject.blog.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "user")
@EqualsAndHashCode
public class UserVO implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer userId; // 會員編號

	@Column(unique = true, nullable = false)
	private String username; // 會員帳號

	@Column(nullable = false)
	private String password; // 會員密碼
	private String name; // 會員姓名
	private String email; // 會員Email
	private String mobile; // 會員電話
	private LocalDate birthday; // 生日
	private LocalDateTime regTime; // 註冊時間
	private LocalDateTime lastLoginTime; // 最後登入時間

}
