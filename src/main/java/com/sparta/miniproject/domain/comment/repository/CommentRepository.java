package com.sparta.miniproject.domain.comment.repository;

import com.sparta.miniproject.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
