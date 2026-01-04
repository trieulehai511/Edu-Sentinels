package com.chrollo_dev.EduSentinel.modules.auth.controller;

import com.chrollo_dev.EduSentinel.common.dto.APIResponse;
import com.chrollo_dev.EduSentinel.modules.auth.dto.AuthenticationResponse;
import com.chrollo_dev.EduSentinel.modules.auth.dto.LoginRequest;
import com.chrollo_dev.EduSentinel.modules.auth.dto.RegisterRequest;
import com.chrollo_dev.EduSentinel.modules.auth.serivce.AuthService;
import com.chrollo_dev.EduSentinel.modules.user.dto.UserResponse;
import com.chrollo_dev.EduSentinel.modules.user.entity.User;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthController {

    AuthService authService;

    @PostMapping("/register")
    APIResponse<UserResponse> register(@RequestBody  @Valid  RegisterRequest registerRequest){
            return APIResponse.<UserResponse>builder().result(authService.register(registerRequest)).build();
    }

    @PostMapping("/login")
    APIResponse<AuthenticationResponse> login(@RequestBody @Valid LoginRequest loginRequest){
        return APIResponse.<AuthenticationResponse>builder().result(authService.login(loginRequest)).build();
    }
}
