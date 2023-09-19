package com.sparta.miniproject.domain.post.service;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.entity.LikePost;
import com.sparta.miniproject.domain.post.repository.LikePostRepository;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import com.sparta.miniproject.global.dto.ResponseMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final LikePostRepository likePostRepository;

    // 전체 조회
    public Page<String> getPost() {
        return postRepository.findTitles(Pageable.ofSize(5));
    }

    // 상세 조회
    public PostResponseDto getPostById(Long postId) {
        Post post = postRepository.findPostById(postId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));
        return new PostResponseDto(post);
    }

    // 생성
    @Transactional
    public ResponseEntity<ResponseMessage> createPost(PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = postRepository.save(new Post(postRequestDto, userEntity));
        ResponseMessage message = new ResponseMessage(HttpStatus.OK.value(), "게시글 작성 완료");
        return ResponseEntity.status(200).body(message);
    }

    // 검색 메소드
    private Post findPost(Long postId) {
        return postRepository.findPostById(postId).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    // 수정
    @Transactional
    public ResponseEntity<ResponseMessage> updatePost(Long postId, PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = findPost(postId);

        // 관리자, 유저 권한 확인
        if (userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(post.getUserEntity().getId())) {
            post.update(postRequestDto, userEntity);
            ResponseMessage message = new ResponseMessage(HttpStatus.OK.value(), "게시글 수정 완료");
            return ResponseEntity.status(200).body(message);
        }
            return ResponseEntity.status(401).body(new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "수정 권한이 없습니다"));

    }

    // 삭제
    public ResponseEntity<ResponseMessage> deletePost(Long postId, UserEntity userEntity) {
        Post post = findPost(postId);

        // 관리자, 유저 권한 확인
        if (userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(post.getUserEntity().getId())) {
            postRepository.delete(post);
            ResponseMessage message = new ResponseMessage(HttpStatus.OK.value(), "게시글 삭제 완료");
            return ResponseEntity.status(200).body(message);
        } else {
            return ResponseEntity.status(401).body(new ResponseMessage(HttpStatus.UNAUTHORIZED.value(), "삭제 권한이 없습니다"));
        }
    }

    // 게시글 좋아요, 취소
    public ResponseEntity<ResponseMessage> likePost(Long postId, UserEntity userEntity) {
        Post post = findPost(postId);
        Optional<LikePost> like = likePostRepository.findByPostIdAndUserEntityId(postId, userEntity.getId());
        if (like.isEmpty()) {
            likePostRepository.save(new LikePost(userEntity, post));
            ResponseMessage message = new ResponseMessage(HttpStatus.OK.value(), "게시글 좋아요");
            return ResponseEntity.status(200).body(message);
        }
        likePostRepository.delete(like.get());
        return ResponseEntity.status(200).body(new ResponseMessage(HttpStatus.OK.value(), "게시글 좋아요 취소"));
    }
}
