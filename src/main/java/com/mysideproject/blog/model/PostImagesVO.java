package com.mysideproject.blog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "postImages")
public class PostImagesVO implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer imageId;
	@ManyToOne
	@JoinColumn(name = "postId")
	private PostVO PostVO;	//FK
	private String imagesUrl;

}
