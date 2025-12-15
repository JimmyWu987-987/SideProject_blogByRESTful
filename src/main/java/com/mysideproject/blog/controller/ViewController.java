package com.mysideproject.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
	
	@GetMapping("/blog/posts")
	public String showPostListPage() {
		return "forward:/front_end/blog/post/searchAllPost.html";
	}

}
