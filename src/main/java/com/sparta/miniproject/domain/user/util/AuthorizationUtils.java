package com.sparta.miniproject.domain.user.util;

import com.sparta.miniproject.domain.user.entity.UserEntity;
import com.sparta.miniproject.domain.user.entity.UserRoleEnum;

public class AuthorizationUtils {
    public static boolean isAuthorized(UserEntity userEntity, UserEntity targetUserEntity) {
        return userEntity.getRole() == UserRoleEnum.ADMIN || userEntity.getId().equals(targetUserEntity.getId());
    }
}
