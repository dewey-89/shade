package com.sparta.miniproject.domain.comment.service;

import com.sparta.miniproject.domain.comment.dto.CommentRequestDto;
import com.sparta.miniproject.domain.comment.dto.CommentResponseDto;
import com.sparta.miniproject.domain.comment.entity.Comment;
import com.sparta.miniproject.domain.comment.repository.CommentRepository;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        // 코멘트를 작성할 포스트를 찾음
        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("포스트가 존재하지 않습니다."));

        // 코멘트를 생성하고 저장
        Comment comment = new Comment(commentRequestDto, post, user);

        commentRepository.save(comment); // 코멘트 저장 후 comment 변수에 저장

        return new CommentResponseDto(comment);
    }

}
