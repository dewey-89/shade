package com.sparta.miniproject.domain.comment.controller;

import com.sparta.miniproject.domain.comment.dto.CommentRequestDto;
import com.sparta.miniproject.domain.comment.dto.CommentResponseDto;
import com.sparta.miniproject.domain.comment.service.CommentService;
import com.sparta.miniproject.domain.user.security.UserDetailsImpl;
import com.sparta.miniproject.global.dto.ResponseMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 작성 API
    @Operation(summary = "댓글 생성",
            responses = {
                    @ApiResponse(description = "성공", responseCode = "200"), @ApiResponse(description = "실패", responseCode = "400")})
    @PostMapping("/comment")
    public ResponseEntity<CommentResponseDto> createComment(
            @RequestBody CommentRequestDto commentRequestDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.createComment(commentRequestDto, userDetails.getUser());
    }

    // 2. 댓글 수정 API
    @PutMapping("/comment/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequestDto updatedCommentDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.updateComment(commentId, updatedCommentDto, userDetails.getUser());
    }
    // 3. 댓글 삭제 API
    @DeleteMapping("/comment/{commentId}")
    public ResponseEntity<ResponseMessage> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return commentService.deleteComment(commentId, userDetails.getUser());
    }

}

