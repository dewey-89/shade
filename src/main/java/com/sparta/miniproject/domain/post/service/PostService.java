package com.sparta.miniproject.domain.post.service;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.entity.PostLike;
import com.sparta.miniproject.domain.post.repository.PostLikeRepository;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    // 전체 조회
    public List<PostResponseDto> getPost() {
        return postRepository.findAllByOrderByModifiedAtDesc().stream().map(PostResponseDto::new).toList();
    }

    // 상세 조회
    public PostResponseDto getPostById(Long id) {
        Post post = postRepository.findPostById(id).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다"));
        return new PostResponseDto(post);
    }

    // 생성
    @Transactional
    public PostResponseDto createPost(PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = new Post(postRequestDto, userEntity);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    // 검색 메소드
    private Post findPost(Long id) {
        return postRepository.findPostById(id).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    // 수정
    @Transactional
    public ResponseEntity<String> updatePost(Long id, PostRequestDto postRequestDto, UserEntity userEntity) {
        Post post = findPost(id);

        // 관리자, 유저 권한 확인
        if (userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(post.getUserEntity().getId())) {
            post.update(postRequestDto, userEntity);
            return new ResponseEntity<>("게시글 수정 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("수정 권한이 없습니다", HttpStatus.UNAUTHORIZED);
        }
    }

    // 삭제
    public ResponseEntity<String> deletePost(Long id, UserEntity userEntity) {
        Post post = findPost(id);

        // 관리자, 유저 권한 확인
        if (userEntity.getRole().equals(UserRoleEnum.ADMIN) || userEntity.getId().equals(post.getUserEntity().getId())) {
            postRepository.delete(post);
            return new ResponseEntity<>("게시글 삭제 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("삭제 권한이 없습니다", HttpStatus.UNAUTHORIZED);
        }
    }

    // 게시글 좋아요, 취소
    public ResponseEntity<String> likePost(Long id, UserEntity userEntity) {
        Post post = findPost(id);
        Optional<PostLike> like = postLikeRepository.findByPostIdAndUserId(id, userEntity.getId());
        if (like.isEmpty()) {
            postLikeRepository.save(new PostLike(userEntity, post));
            return new ResponseEntity<>("게시글 좋아요", HttpStatus.OK);
        }
        postLikeRepository.delete(like.get());
        return new ResponseEntity<>("게시글 좋아요 취소", HttpStatus.OK);
    }
}
