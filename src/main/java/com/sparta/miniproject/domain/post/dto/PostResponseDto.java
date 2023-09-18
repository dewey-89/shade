package com.sparta.miniproject.domain.post.dto;

import com.sparta.miniproject.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private Long id;

    private String nickname;
    private String title;
    private String content;

    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    private Integer likeCount;


    public PostResponseDto(Post post) {
        this.id = post.getId();

        this.nickname = post.getUserEntity().getNickname();
        this.title = post.getTitle();
        this.content = post.getContent();

        this.createdAt = post.getCreatedAt();
        this.modifiedAt = post.getModifiedAt();

        this.likeCount = post.getLikePostList().size();
    }
}
