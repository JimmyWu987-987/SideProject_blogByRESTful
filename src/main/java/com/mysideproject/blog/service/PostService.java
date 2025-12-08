package com.mysideproject.blog.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.mysideproject.blog.Repository.PostRepository;
import com.mysideproject.blog.model.CustomUserDetails;
import com.mysideproject.blog.model.PostVO;

@Service("postService")
public class PostService {

	@Autowired
	PostRepository repository;

	// ============ CRUD ============
	public void addPost(PostVO postVO, Integer userId) {

		postVO.setUserId(userId);
		postVO.setCreateTime(LocalDateTime.now());
		postVO.setOnAndOff((byte)0); // 預設下架
		

		repository.save(postVO);
	}

	public void updatePost(PostVO postVO, CustomUserDetails userDetails) {
		
		Optional<PostVO> optional = repository.findById(postVO.getPostId());
		
		if(!optional.isPresent()) {
			throw new IllegalArgumentException("文章ID不存在！ID編號：" + postVO.getPostId());
		}
		
		PostVO existingPost = optional.get();
		Integer userId = userDetails.getUserId();
		
		
		// 檢查是否為作者本人或者管理員
		boolean isAuther = existingPost.getUserId().equals(userId);
		// boolean isRoot = userDetails.isRoot();
		
		// 確認當前登入用戶是否為作者本人
		// 管理員不能修改文章內容
		if(!isAuther) {
			throw new AccessDeniedException("您沒有權限修改這篇文章 (ID: " + postVO.getPostId() + ")，因為您不是作者。");
		}
		
		// 確保更新的文章保留原作者 ID (防止客戶端篡改 authorId)
		
		postVO.setUserId(existingPost.getUserId());
		postVO.setOnAndOff(existingPost.getOnAndOff());
		// postVO.setPostImagesVO(existingPost.getPostImagesVO());
		
		postVO.setCreateTime(existingPost.getCreateTime());
		// 設定最後更新時間
		postVO.setLastUpdateTime(LocalDateTime.now());
		
		// 更新文章
		repository.save(postVO);
	}

	public void deletePost(Integer postId, CustomUserDetails userDetails) {
		
		Optional<PostVO> optional = repository.findById(postId);
		
		if (!optional.isPresent()) {
            throw new IllegalArgumentException("文章ID不存在！ID編號：" + postId);
        }
		
		PostVO existingPost = optional.get();
		Integer userId = userDetails.getUserId();
		
		// 檢查是否為作者本人或者管理員
		boolean isAuther = existingPost.getUserId().equals(userId);
		boolean isRoot = userDetails.isRoot();
		
		// 確認當前登入用戶是否為作者本人還是管理者
		if(!isAuther && !isRoot) {
			throw new AccessDeniedException("您沒有權限刪除這篇文章 (ID: " + postId + ")，因為您不是作者。");
		}
		
		// 刪除文章
		repository.deleteById(postId);
	}

//	public List<PostVO> getAll(){
//		return repository.findAll();
//	}

//	有分頁功能的查詢全部文章
//	Pageable (介面)： 告訴資料庫「我要第 N 頁，每頁 M 筆資料，並且要依 Z 欄位排序」。
//	Page<T> (類別)： 這是 查詢結果。它不僅包含當前頁的資料 (List<T>)，還包含許多分頁元資訊 (Metadata)，例如：
//	總頁數 (getTotalPages())
//	總筆數 (getTotalElements())
//	當前頁碼 (getNumber())
//	是否為第一頁/最後一頁 (isFirst(), isLast())
	public Page<PostVO> getAllByPage(Pageable pageable) {

		// 返回的是一個 Page 物件，其中包含分頁資訊和文章列表
		return repository.findAll(pageable);
	}

	public PostVO getOneByPostId(Integer postId) {
		Optional<PostVO> optional = repository.findById(postId);
		return optional.orElseThrow(() -> new IllegalArgumentException("文章ID不存在！ID編號：" + postId)); // The else is NoSuchElementException.
	}
}
