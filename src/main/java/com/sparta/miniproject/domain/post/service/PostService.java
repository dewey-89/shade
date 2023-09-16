package com.sparta.miniproject.domain.post.service;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.User;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {


    private final PostRepository postRepository;

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
    public PostResponseDto createPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto, user);
        postRepository.save(post);
        return new PostResponseDto(post);
    }

    // 검색 메소드
    private Post findPost(Long id) {
        return postRepository.findPostById(id).orElseThrow(() -> new RuntimeException("게시글이 존재하지 않습니다."));
    }

    // 수정
    @Transactional
    public ResponseEntity<String> updatePost(Long id, PostRequestDto postRequestDto, User user) {
        Post post = findPost(id);

        // 관리자, 유저 권한 확인
        if (user.getRole().equals(UserRoleEnum.ADMIN) || user.getId().equals(post.getUser().getId())) {
            post.update(postRequestDto, user);
            return new ResponseEntity<>("게시글 수정 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("수정 권한이 없습니다", HttpStatus.UNAUTHORIZED);
        }
    }

    // 삭제
    public ResponseEntity<String> deletePost(Long id, User user) {
        Post post = findPost(id);

        // 관리자, 유저 권한 확인
        if (user.getRole().equals(UserRoleEnum.ADMIN) || user.getId().equals(post.getUser().getId())) {
            postRepository.delete(post);
            return new ResponseEntity<>("게시글 삭제 완료", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("삭제 권한이 없습니다", HttpStatus.UNAUTHORIZED);
        }
    }
}
