package com.sparta.miniproject.domain.post.service;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.entity.LikePost;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.LikePostRepository;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import com.sparta.miniproject.global.dto.ApiResponse;
import com.sparta.miniproject.global.exception.CustomException;
import com.sparta.miniproject.global.exception.ErrrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;

    // 전체 조회
    public ResponseEntity<ApiResponse<Page<PostResponseDto>>> getAllPosts(int page, int size) {
        Page<Post> postList = postRepository.findAllByOrderByModifiedAtDesc(Pageable.ofSize(size).withPage(page-1));
        Page<PostResponseDto> postResponseDtos = postList.map(PostResponseDto::new);
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(postResponseDtos));
    }

    // 상세 조회
    public ApiResponse<PostResponseDto> getPostById(Long postId) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new CustomException(ErrrorCode.NOT_FOUND_POST));
        return ApiResponse.successData(new PostResponseDto(post));
    }

    // 생성
    @Transactional
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = postRepository.save(new Post(postRequestDto, userEntity));
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.successData(new PostResponseDto(post)));
    }

    // 수정
    @Transactional
    public ApiResponse<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = findPost(postId);

        // 관리자, 유저 권한 확인
        if (!(userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(post.getUserEntity().getId()))) {
            throw new CustomException(ErrrorCode.NOT_AUTHORIZED);
        }
        post.update(postRequestDto, userEntity);
        return ApiResponse.successData(new PostResponseDto(post));

    }

    // 삭제
    @Transactional
    public ApiResponse<String> deletePost(Long postId, UserEntity userEntity) {
        Post post = findPost(postId);

        // 관리자, 유저 권한 확인
        if (!(userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(post.getUserEntity().getId()))) {
            throw new CustomException(ErrrorCode.NOT_AUTHORIZED);
        }
        postRepository.delete(post);
        return ApiResponse.successMessage("게시글이 삭제되었습니다");
    }

    // 게시글 좋아요, 취소
    @Transactional
    public ApiResponse<String> likePost(Long postId, UserEntity userEntity) {
        Post post = findPost(postId);
        Optional<LikePost> like = likePostRepository.findByPostIdAndUserEntityId(postId, userEntity.getId());
        if (like.isEmpty()) {
            likePostRepository.save(new LikePost(userEntity, post));
            return ApiResponse.successMessage("게시글 좋아요");
        }
        likePostRepository.delete(like.get());
        return ApiResponse.successMessage("게시글 취소");
    }

    // 검색 메소드
    private Post findPost(Long postId) {
        return postRepository.findPostById(postId).orElseThrow(() -> new CustomException(ErrrorCode.NOT_FOUND_POST));
    }
}