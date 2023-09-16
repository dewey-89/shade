package com.sparta.miniproject.domain.post.service;

import com.sparta.miniproject.domain.post.dto.PostRequestDto;
import com.sparta.miniproject.domain.post.dto.PostResponseDto;
import com.sparta.miniproject.domain.post.entity.Post;
import com.sparta.miniproject.domain.post.repository.PostRepository;
import com.sparta.miniproject.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
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
}
