package com.sparta.miniproject.domain.user.repository;

import com.sparta.miniproject.domain.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByNickname(String nickname);
    Optional<UserEntity> findByEmail(String email);
}
