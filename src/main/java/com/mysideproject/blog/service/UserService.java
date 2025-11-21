package com.mysideproject.blog.service;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mysideproject.blog.Repository.UserRepository;
import com.mysideproject.blog.dto.RegUserDTO;
import com.mysideproject.blog.exception.DuplicateUsernameException;
import com.mysideproject.blog.model.UserVO;

@Service
public class UserService {
	
	@Autowired
	private UserRepository repository;
	
	// 註冊用
	// 將會員密碼編碼存入資料庫用
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	// =========== 註冊會員（新增會員）===========
	public void registerUser(RegUserDTO regUserDTO) {
		// 1. 檢查帳號是否存在（防止重複註冊）
		if(repository.findByUsername(regUserDTO.getUsername()).isPresent()) {
			throw new DuplicateUsernameException("Username is already taken! 帳號名已被使用！");
		}
		
		// 2. 創建 UserVO 物件並對密碼進行加密
		UserVO userVO = new UserVO();
		// 將 regUserDTO 資料拷貝到 userVO
		userVO.setUsername(regUserDTO.getUsername());
		// 對密碼進行 BCrypt 加密
		userVO.setPassword(passwordEncoder.encode(regUserDTO.getPassword()));
		userVO.setName(regUserDTO.getName());
		userVO.setEmail(regUserDTO.getEmail());
		userVO.setMobile(regUserDTO.getMobile());
		userVO.setBirthday(regUserDTO.getBirthday());
		// 依照伺服器的時間為註冊時間
		userVO.setRegTime(LocalDateTime.now());
		
		// 儲存至 DB
		repository.save(userVO);
	}
	
	
	
	
}
