package com.chrollo_dev.EduSentinel.modules.user.dto;

import com.chrollo_dev.EduSentinel.modules.user.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    String id;
    String username;
    String fullName;
    UserRole role;
    private String email;
}
