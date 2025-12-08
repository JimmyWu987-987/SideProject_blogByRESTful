package com.mysideproject.blog.model;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "post")
public class PostVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer postId;	// 文章id
	private Integer userId; // 文章作者
	private String title;	// 文章標題
	private String content;	// 文章內容
	private LocalDateTime createTime;	// 文章建立時間	
	private LocalDateTime lastUpdateTime; // 文章最後修改的時間
	private Byte onAndOff; // 文章上下架狀態（0: 下架; 1: 上架）
	// @OneToMany(mappedBy = "PostVO")
	// private List<PostImagesVO> postImagesVO; 	// 文章照片
	
	// 未來會增加先備註
	// 文章分類
	// etc...
	
	
}
