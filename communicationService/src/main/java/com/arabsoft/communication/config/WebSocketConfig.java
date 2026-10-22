package com.arabsoft.communication.config;

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
        // Topic prefix for subscriptions (e.g. /topic/messages/{userId}, /topic/public, /topic/user-status)
        config.enableSimpleBroker("/topic");
        // Application destination prefix for incoming messages from client (e.g. /app/chat.sendMessage)
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Register WebSocket /ws endpoint with SockJS fallback support
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .withSockJS();

        // Plain WebSocket endpoint without SockJS as secondary fallback
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
    }
}
