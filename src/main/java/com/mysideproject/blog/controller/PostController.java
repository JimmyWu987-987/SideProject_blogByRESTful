package com.mysideproject.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysideproject.blog.model.CustomUserDetails;
import com.mysideproject.blog.model.PostVO;
import com.mysideproject.blog.service.PostService;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
	
	@Autowired
	PostService postSvc;
	
	
	@GetMapping
	public Page<PostVO> getAllPosts(Pageable pageable) {
		// Pageable 自動從 URL 參數，分析出分頁和排序資料
		// 參數名預設是 page 和 size
		
		return postSvc.getAllByPage(pageable);
	}
	
	// 使用 ResponseEntity 包裝做回傳
	// ============= 新增文章 =============
	@PostMapping
	public ResponseEntity<PostVO> addPost(@RequestBody PostVO postVO,
										  Authentication authentication) {
		// 1. 從 Spring Security 獲取當前用戶 ID
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = customUserDetails.getUserId();							  
		
		// 2. 在 Service 層處理：確保登入帳號，才能新增文章
		postSvc.addPost(postVO, userId);
		
		// 回傳 201 給 Client 端
		return new ResponseEntity<PostVO>(postVO, HttpStatus.CREATED);
	}
	// ============= 修改文章 =============
	@PutMapping("/{id}")
	public PostVO updatePost(@PathVariable("id") Integer postId,
							 @RequestBody PostVO postVO,
							 Authentication authentication) {
		
		// 1. 從 Spring Security 獲取當前用戶 ID
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = customUserDetails.getUserId();
		
		// 2. 為了安全性，確保傳入的 PostVO ID 與 PathVariable 相同
		postVO.setPostId(postId);
		
		// 3. 在 Service 層處理：確保只有文章作者才能修改
        postSvc.updatePost(postVO, userId);
		
		return postVO;
	}
	
	// ============= 刪除文章 =============
	@DeleteMapping("/{id}")
	public String deletePost(@PathVariable("id") Integer postId,
							 Authentication authentication) {
		// 1. 從 Spring Security 獲取當前用戶 ID
		CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
		Integer userId = customUserDetails.getUserId();
		
		// 2. 在 Service 層處理：確保只有文章作者才能刪除
        postSvc.deletePost(postId, userId);
		
		return "文章編號：" + postId + " 刪除成功";
	}
}
