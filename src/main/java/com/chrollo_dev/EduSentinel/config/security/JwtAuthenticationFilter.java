package com.chrollo_dev.EduSentinel.config.security;


import com.chrollo_dev.EduSentinel.modules.auth.serivce.AuthService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final AuthService authService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Lấy Token từ Header "Authorization"
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response); // Không có token thì cho qua (để Security Config chặn sau)
            return;
        }

        String token = authHeader.substring(7); // Cắt bỏ chữ "Bearer "

        try {
            // 2. Nhờ AuthService kiểm tra Token
            var signedJWT = authService.verifyToken(token);

            // 3. Lấy thông tin (Username, Role) từ Token
            String username = signedJWT.getJWTClaimsSet().getSubject();
            String role = signedJWT.getJWTClaimsSet().getStringClaim("scope"); // Lấy role đã lưu lúc login

            // 4. Tạo đối tượng Authentication để báo cho Spring Security biết "Thằng này uy tín"
            // Lưu ý: Spring Security cần Role có dạng "ROLE_STUDENT"
            var authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // 5. Set vào Context (Đăng nhập thành công cho Request này)
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            log.error("Authentication Failed: {}", e.getMessage());
            // Nếu Token lỗi, SecurityContext sẽ rỗng -> Spring tự trả về 401
        }

        filterChain.doFilter(request, response);
    }
}
