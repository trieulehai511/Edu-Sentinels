package com.chrollo_dev.EduSentinel.modules.auth.dto;

import com.chrollo_dev.EduSentinel.modules.user.enums.UserRole;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank(message = "Tên người dùng không được để trống")
    @Size(min=6, max = 50, message = "USERNAME_INVALID")
    String username;
    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min=6, max = 50, message = "Tên người dùng ít nhất 6-50 kí tự")
    String password;

    @NotBlank(message = "Nhập mật khẩu lại không được để trống")
    String repeatPassword;

    @NotBlank(message = "Họ tên không được để trống")
    String fullName;

    @Email(message = "Email không hợp lệ")
    String email;

    @NotNull(message = "Phải chọn vai trò (SUPERVISOR hoặc STUDENT)")
    UserRole role;
}
