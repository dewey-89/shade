package com.sparta.miniproject.domain.comment.service;

import com.sparta.miniproject.domain.comment.dto.CommentRequestDto;
import com.sparta.miniproject.domain.comment.dto.CommentResponseDto;
import com.sparta.miniproject.domain.comment.entity.Comment;
import com.sparta.miniproject.domain.comment.repository.CommentRepository;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.User;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // 수정
        @Transactional
        public CommentResponseDto updateComment(Long id, CommentRequestDto commentRequestDto, User user) {
            Comment comment = findComment(id);

            // 댓글 작성자 또는 관리자 권한 확인
            if (user.getRole().equals(UserRoleEnum.ADMIN) || user.getId().equals(comment.getUser().getId())) {
                comment.updateComment(commentRequestDto, user);
                return new CommentResponseDto("댓글 수정이 완료되었습니다.");
            } else {
                throw new UnauthorizedException("댓글 수정 권한이 없습니다.");
            }
        }

    private Comment findComment(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
    }

}

