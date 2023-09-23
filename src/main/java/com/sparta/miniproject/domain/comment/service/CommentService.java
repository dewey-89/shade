package com.sparta.miniproject.domain.comment.service;

import com.sparta.miniproject.domain.comment.dto.CommentRequestDto;
import com.sparta.miniproject.domain.comment.dto.CommentResponseDto;
import com.sparta.miniproject.domain.comment.entity.Comment;
import com.sparta.miniproject.domain.comment.repository.CommentRepository;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.util.AuthorizationUtils;
import com.sparta.miniproject.global.dto.ApiResponse;
import com.sparta.miniproject.global.exception.CustomException;
import com.sparta.miniproject.global.exception.ErrrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public ApiResponse<CommentResponseDto> createComment(Long postId, CommentRequestDto commentRequestDto, UserEntity userEntity) {

        Post post = postRepository.findPostById(postId).orElseThrow(() -> new CustomException(ErrrorCode.NOT_FOUND_POST));

        Comment comment = new Comment(commentRequestDto, post, userEntity);

        commentRepository.save(comment);

        return ApiResponse.successData(new CommentResponseDto(comment));
    }

    // 수정
    @Transactional
    public ApiResponse<CommentResponseDto> updateComment(Long commentId, CommentRequestDto commentRequestDto, UserEntity userEntity) {
        Comment comment = findComment(commentId);

        if (!AuthorizationUtils.isAuthorized(userEntity, comment.getUserEntity())) {
            throw new CustomException(ErrrorCode.NOT_AUTHORIZED);
        }

        comment.updateComment(commentRequestDto);
        return ApiResponse.successData(new CommentResponseDto(comment));
    }

    //삭제
    @Transactional
    public ApiResponse<String> deleteComment(Long commentId, UserEntity userEntity) {
        Comment comment = findComment(commentId);

        // 댓글 작성자 또는 관리자 권한 확인
        if (!AuthorizationUtils.isAuthorized(userEntity, comment.getUserEntity())) {
            throw new CustomException(ErrrorCode.NOT_AUTHORIZED);
        }

        commentRepository.delete(comment);
        return ApiResponse.successMessage("댓글이 삭제되었습니다.");
    }

    private Comment findComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrrorCode.NOT_FOUND_COMMENT));
    }

}

