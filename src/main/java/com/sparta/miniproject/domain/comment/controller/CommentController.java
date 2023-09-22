package com.sparta.miniproject.domain.comment.controller;

import com.sparta.miniproject.domain.comment.dto.CommentRequestDto;
import com.sparta.miniproject.domain.comment.dto.CommentResponseDto;
import com.sparta.miniproject.domain.comment.service.CommentService;
import com.sparta.miniproject.domain.user.security.UserDetailsImpl;
import com.sparta.miniproject.global.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성")
    @PostMapping
    public ApiResponse<CommentResponseDto> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(postId, commentRequestDto, userDetails.getUser());
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ApiResponse<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto updatedCommentDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, updatedCommentDto, userDetails.getUser());
    }

    @Operation(summary = "댓글 삭제")
    @DeleteMapping("/{commentId}")
    public ApiResponse<String> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUser());
    }

}

