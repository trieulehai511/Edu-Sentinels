package com.chrollo_dev.EduSentinel.common.utils;

import com.chrollo_dev.EduSentinel.common.exception.AppException;
import com.chrollo_dev.EduSentinel.common.exception.ErrorCode;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import com.chrollo_dev.EduSentinel.modules.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtils {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || !authentication.isAuthenticated() ||
                authentication.getPrincipal().equals("anonymousUser")){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        String username = authentication.getName();
        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
    }
}
