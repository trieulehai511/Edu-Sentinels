package com.chrollo_dev.EduSentinel.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic");
        // Client gửi lên server qua đường dẫn bắt đầu bằng /app (nếu cần chat)
        config.setApplicationDestinationPrefixes("/app");
    }
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Đây là cái cổng để React kết nối vào
        registry.addEndpoint("/ws-edusentinel")
                .setAllowedOriginPatterns("*") // Cho phép mọi frontend kết nối
                .withSockJS(); // Hỗ trợ fallback nếu trình duyệt không có WebSocket
    }
}
