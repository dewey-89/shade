package com.sparta.miniproject.domain.post.controller;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.service.PostService;
import com.sparta.miniproject.domain.user.security.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 게시글 작성
    @PostMapping("/post")
    public PostResponseDto createPost(
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getUser());
    }
}
