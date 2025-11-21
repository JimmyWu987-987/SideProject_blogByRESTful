package com.mysideproject.blog.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.mysideproject.blog.Repository.PostRepository;
import com.mysideproject.blog.model.PostVO;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {
	
	@Mock
	private PostRepository postRepository;
	
	@InjectMocks
	private PostService postService;
	
	@Test
	void testAddPost_Success() {
		// 1. Arrange (準備)
		PostVO newPost = new PostVO();
		newPost.setTitle("Test Title");
		newPost.setContent("Test Content");
		
		// 模擬新增資料後的結果，資料庫會給PK ID值
		PostVO savedPost = new PostVO();
		savedPost.setPostId(1);
		savedPost.setTitle("Test Title");
		savedPost.setContent("Test Content");
		
		// 設置模擬行為：當 postRepository.save() 被呼叫時，返回 savedPost
		when(postRepository.save(newPost)).thenReturn(savedPost);
		
		// 2. Act (執行)
//		postService.addPost(newPost);
		
		// 3. Assert (驗證)
		// 驗證 postRepository.save() 方法是否被呼叫了一次，並且傳入的參數是 newPost
		verify(postRepository, times(1)).save(newPost);
		
	}
	
	// 文章編號: 10 為存在，成功更新
	@Test
	void testUpdatePost_Success() {
		
		// 1. 準備 Arrange
		PostVO existPost = new PostVO();
		existPost.setPostId(10);
		existPost.setTitle("Update Title");
		existPost.setContent("Update Content");
		
		// 模擬 ID:10 的文章存在
		when(postRepository.existsById(10)).thenReturn(true);
		// 模擬 save 有執行
		when(postRepository.save(existPost)).thenReturn(existPost);
		
		
		// 2. 執行 Act
//		postService.updatePost(existPost);
		
		// 3. 驗證 Assert
		// 驗證 .existsById(10) 呼叫 1 次
		verify(postRepository, times(1)).existsById(10);
		// 驗證 .save() 呼叫 1 次
		verify(postRepository, times(1)).save(existPost);
	}
	
	// 文章不存在，拋出例外
	@Test
	void testUpdatePost_NotFound() {
		// 1. 準備 Arrange
		PostVO nonExistPost = new PostVO();
		nonExistPost.setPostId(999);
		nonExistPost.setTitle("錯誤的標題");
		
		// 假設該文章ID:999 不存在
		when(postRepository.existsById(999)).thenReturn(false);
		
		// 2. 執行 Act
		Exception e = assertThrows(Exception.class, ()->{
//			postService.updatePost(nonExistPost);
		});
		
		assertEquals("文章ID不存在！ID編號：" + nonExistPost.getPostId(), e.getMessage());
		
		// 3. 驗證 Assert
		// .save未被呼叫，因為找不到該文章。
		verify(postRepository, never()).save(nonExistPost);
	}	
	
}	
