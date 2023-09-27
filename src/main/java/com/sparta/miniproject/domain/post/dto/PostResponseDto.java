package com.sparta.miniproject.domain.post.dto;

import com.sparta.miniproject.domain.comment.dto.CommentResponseDto;
import com.sparta.miniproject.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;

    private String nickname;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private Integer likeCount;

    private List<CommentResponseDto> commentsList = new ArrayList<>();


    public PostResponseDto(Post post) {
        this.id = post.getId();

        this.nickname = post.getUserEntity().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();

        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

        this.likeCount = post.getLikePostList().size();

        post.getCommentsList().forEach(comment -> commentsList.add(new CommentResponseDto(comment)));
        Collections.reverse(commentsList);

    }
}
