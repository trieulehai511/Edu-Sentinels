package com.chrollo_dev.EduSentinel.modules.auth.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthenticationResponse {
    private String token;      // Chuỗi JWT
    private boolean authenticated; // Trạng thái (True/False)
}