package com.sparta.miniproject.domain.post.repository;

import com.sparta.miniproject.domain.post.entity.LikePost;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikePostRepository extends JpaRepository<LikePost, Long> {
    Optional<LikePost> findByPostIdAndUserId(Long postId, Long userId);
}
