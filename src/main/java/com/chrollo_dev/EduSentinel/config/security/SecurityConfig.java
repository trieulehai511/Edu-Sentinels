package com.chrollo_dev.EduSentinel.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Tắt CSRF vì chúng ta dùng REST API (Stateless), không dùng Session Cookie truyền thống
                .csrf(AbstractHttpConfigurer::disable)

                // Cấu hình quyền truy cập
                .authorizeHttpRequests(auth -> auth
                        // Cho phép API Auth (Login/Register) hoạt động không cần token
                        .requestMatchers("/auth/**").permitAll()

                        // Cho phép endpoint WebSocket để bắt tay (Handshake) ban đầu
                        .requestMatchers("/ws-edusentinel/**").permitAll()

                        // Các request khác bắt buộc phải có Token (Sẽ làm sau)
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        return http.build();
    }
}
