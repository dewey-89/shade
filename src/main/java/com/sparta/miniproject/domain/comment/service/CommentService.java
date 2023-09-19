package com.sparta.miniproject.domain.comment.service;

import com.sparta.miniproject.domain.comment.dto.CommentRequestDto;
import com.sparta.miniproject.domain.comment.dto.CommentResponseDto;
import com.sparta.miniproject.domain.comment.entity.Comment;
import com.sparta.miniproject.domain.comment.repository.CommentRepository;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import com.sparta.miniproject.global.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ResponseEntity<CommentResponseDto> createComment(CommentRequestDto commentRequestDto, UserEntity userEntity) {
        // 코멘트를 작성할 포스트를 찾음
        Post post = postRepository.findById(commentRequestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("포스트가 존재하지 않습니다."));

        // 코멘트를 생성하고 저장
        Comment comment = new Comment(commentRequestDto, post, userEntity);

        commentRepository.save(comment); // 코멘트 저장 후 comment 변수에 저장

        return ResponseEntity.status(200).body(new CommentResponseDto(comment));
    }

    // 수정
        @Transactional
        public ResponseEntity<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto, UserEntity userEntity) {
            Comment comment = findComment(commentId);

            // 댓글 작성자 또는 관리자 권한 확인
            if (!(userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(comment.getUserEntity().getId()))) {
                throw new RuntimeException("댓글 수정 권한이 없습니다.");
            }

            comment.updateComment(commentRequestDto);
            commentRepository.save(comment);
            return ResponseEntity.status(200).body(new CommentResponseDto(comment));
        }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글이 존재하지 않습니다."));
    }


    //삭제
    @Transactional
    public ResponseEntity<ResponseMessage> deleteComment(Long commentId, UserEntity userEntity) {
        Comment comment = findComment(commentId);

        // 댓글 작성자 또는 관리자 권한 확인
        if (!(userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(comment.getUserEntity().getId()))) {
            throw new RuntimeException("댓글 삭제 권한이 없습니다.");
        }

        commentRepository.delete(comment);
        return ResponseEntity.status(200).body(new ResponseMessage(200, "댓글 삭제 성공"));
    }

}

