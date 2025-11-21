package com.mysideproject.blog.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.mysideproject.blog.model.PostVO;

@Repository
public interface PostRepository extends JpaRepository<PostVO, Integer>{
	
}
