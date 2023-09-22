package com.sparta.miniproject.domain.post.entity;

import com.sparta.miniproject.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Table(name = "post_likes")
@NoArgsConstructor
public class LikePost {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    public LikePost(UserEntity userEntity, Post post) {
        this.userEntity = userEntity;
        this.post = post;
    }
}
