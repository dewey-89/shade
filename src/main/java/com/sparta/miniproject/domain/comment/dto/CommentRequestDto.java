package com.sparta.miniproject.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private String comment;
    private Long postId;
}
