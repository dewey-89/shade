package com.sparta.miniproject.domain.post.controller;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.service.PostService;
import com.sparta.miniproject.domain.user.security.UserDetailsImpl;
import com.sparta.miniproject.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @Operation(summary = "게시글 전체 조회")
    @GetMapping
    public ApiResponse<Page<PostResponseDto>> getAllPosts(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "20") int size
    ){
        return postService.getAllPosts(page, size);
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    public ApiResponse<PostResponseDto> getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    @Operation(summary = "게시글 작성")
    @PostMapping
    public ApiResponse<PostResponseDto> createPost(
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getUser());
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{postId}")
    public ApiResponse<PostResponseDto> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, postRequestDto, userDetails.getUser());
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ApiResponse<String> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getUser());
    }

    @Operation(summary = "게시글 좋아요/취소")
    @PutMapping("/{postId}/like")
    public ApiResponse<String> togglePostLike(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.togglePostLike(postId, userDetails.getUser());
    }
}
