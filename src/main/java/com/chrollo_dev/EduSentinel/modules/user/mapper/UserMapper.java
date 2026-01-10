package com.chrollo_dev.EduSentinel.modules.user.mapper;

import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toUserResponse(User user) {
        if (user == null) return null;

        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}
