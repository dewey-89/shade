package com.sparta.miniproject.domain.post.controller;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.service.PostService;
import com.sparta.miniproject.domain.user.security.UserDetailsImpl;
import com.sparta.miniproject.global.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시글 전체 조회
    @GetMapping("/post")
    public List<PostResponseDto> getPost() {
        return postService.getPost();
    }

    // 게시글 상세 조회
    @GetMapping("/post/{postId}")
    public PostResponseDto getPostById(@PathVariable Long postId) {
        return postService.getPostById(postId);
    }

    // 게시글 작성
    @PostMapping("/post")
    public ResponseEntity<ResponseMessage> createPost(
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.createPost(postRequestDto, userDetails.getUser());
    }

    // 게시글 수정
    @PutMapping("/post/{postId}")
    public ResponseEntity<ResponseMessage> updatePost(
            @PathVariable Long postId,
            @RequestBody PostRequestDto postRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.updatePost(postId, postRequestDto, userDetails.getUser());
    }

    // 게시글 삭제
    @DeleteMapping("/post/{postId}")
    public ResponseEntity<ResponseMessage> deletePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.deletePost(postId, userDetails.getUser());
    }

    // 게시글 좋아요
    @Operation(summary = "게시글 좋아요/취소")
    @PutMapping("/post/{postId}/like")
    public ResponseEntity<ResponseMessage> likePost(
            @PathVariable Long postId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return postService.likePost(postId, userDetails.getUser());
    }
}
