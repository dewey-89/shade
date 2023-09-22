package com.sparta.miniproject.domain.post.repository;

import com.sparta.miniproject.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findAllByOrderByModifiedAtDesc(Pageable pageable);

    Optional<Post> findPostById(Long id);
}
