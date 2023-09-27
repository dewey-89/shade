package com.sparta.miniproject.domain.post.entity;

import com.sparta.miniproject.domain.comment.entity.Comment;
import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.global.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "posts")
public class Post extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 500)
    private String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentsList = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<LikePost> likePostList = new ArrayList<>();


    public Post(PostRequestDto postRequestDto, UserEntity userEntity) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.userEntity = userEntity;
    }

    public void update(PostRequestDto postRequestDto, UserEntity userEntity) {
        this.title = postRequestDto.getTitle();
        this.content = postRequestDto.getContent();
        this.userEntity = userEntity;
    }
}