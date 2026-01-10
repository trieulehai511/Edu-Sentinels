package com.chrollo_dev.EduSentinel.modules.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Tên người dùng không được để trống\"")
    String username;

    @NotBlank(message = "Mật khẩu không được để trống\"")
    String password;
}
