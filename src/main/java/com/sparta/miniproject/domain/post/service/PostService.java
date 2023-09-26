package com.sparta.miniproject.domain.post.service;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.entity.LikePost;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.LikePostRepository;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.util.AuthorizationUtils;
import com.sparta.miniproject.global.dto.ApiResponse;
import com.sparta.miniproject.global.exception.CustomException;
import com.sparta.miniproject.global.exception.ErrrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;


    public ApiResponse<Page<PostResponseDto>> getAllPosts(int page, int size) {
        Page<Post> postList = postRepository.findAllByOrderByModifiedAtDesc(Pageable.ofSize(size).withPage(page));
        Page<PostResponseDto> postResponseDtos = postList.map(PostResponseDto::new);
        return ApiResponse.successData(postResponseDtos);
    }

    public ApiResponse<PostResponseDto> getPostById(Long postId) {
        Post post = findPost(postId);
        return ApiResponse.successData(new PostResponseDto(post));
    }

    @Transactional
    public ApiResponse<PostResponseDto> createPost(PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = postRepository.save(new Post(postRequestDto, userEntity));
        return ApiResponse.successData(new PostResponseDto(post));
    }

    @Transactional
    public ApiResponse<PostResponseDto> updatePost(Long postId, PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = findPost(postId);

        if (!AuthorizationUtils.isAuthorized(userEntity, post.getUserEntity())) {
            throw new CustomException(ErrrorCode.NOT_AUTHORIZED);
        }

        post.update(postRequestDto, userEntity);
        return ApiResponse.successData(new PostResponseDto(post));

    }

    @Transactional
    public ApiResponse<String> deletePost(Long postId, UserEntity userEntity) {
        Post post = findPost(postId);

        if (!AuthorizationUtils.isAuthorized(userEntity, post.getUserEntity())) {
            throw new CustomException(ErrrorCode.NOT_AUTHORIZED);
        }
        postRepository.delete(post);
        return ApiResponse.successMessage("게시글이 삭제되었습니다");
    }

    @Transactional
    public ApiResponse<String> togglePostLike(Long postId, UserEntity userEntity) {
        Post post = findPost(postId);
        Optional<LikePost> like = likePostRepository.findByPostIdAndUserEntityId(postId, userEntity.getId());

        if (like.isEmpty()) {
            likePostRepository.save(new LikePost(userEntity, post));
            return ApiResponse.successMessage("게시글 좋아요");
        }

        likePostRepository.delete(like.get());
        return ApiResponse.successMessage("게시글 좋아요 취소");
    }

    private Post findPost(Long postId) {
        return postRepository.findPostById(postId)
                .orElseThrow(() -> new CustomException(ErrrorCode.NOT_FOUND_POST));
    }
}