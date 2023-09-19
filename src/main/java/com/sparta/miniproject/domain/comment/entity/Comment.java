package com.sparta.miniproject.domain.comment.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sparta.miniproject.domain.comment.dto.CommentRequestDto;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.global.entity.Timestamped;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 500)
    private String comment;

    @ManyToOne
    @JoinColumn(name = "user_id",nullable = false)
    private UserEntity userEntity;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public Comment(CommentRequestDto commentRequestDto, Post post, UserEntity userEntity) {
        this.comment = commentRequestDto.getComment();
        this.post = post;
        this.userEntity = userEntity;
    }


    public void updateComment(CommentRequestDto commentRequestDto) {
        this.comment = commentRequestDto.getComment();
    }
}


