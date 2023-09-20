package com.sparta.miniproject.domain.post.repository;

import com.sparta.miniproject.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface PostRepository extends JpaRepository<Post, Long> {

    @Query("SELECT p.id, p.title FROM Post p ORDER BY p.modifiedAt DESC")
    Page<String> findTitles(Pageable pageable);

    Optional<Post> findPostById(Long id);
}
