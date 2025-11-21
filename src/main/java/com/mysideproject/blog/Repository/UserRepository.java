package com.mysideproject.blog.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysideproject.blog.model.UserVO;

@Repository
public interface UserRepository extends JpaRepository<UserVO, Integer>{
	
	Optional<UserVO> findByUsername(String username);
}
