package com.mysideproject.blog.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.mysideproject.blog.Repository.UserRepository;
import com.mysideproject.blog.model.CustomUserDetails;
import com.mysideproject.blog.model.UserVO;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<UserVO> optional = userRepository.findByUsername(username);

		UserVO userVO = optional
				.orElseThrow(() -> new UsernameNotFoundException("查無此帳號！" + username));
		
//		UserVO userVO = optional.orElse(null);
//		if (userVO == null || username.isEmpty()) {
//			throw new UsernameNotFoundException("查無此帳號！" + username);
//		}

		return new CustomUserDetails(userVO);
	}

}
