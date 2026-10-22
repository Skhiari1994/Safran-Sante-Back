package com.arabsoft.communication.controller;

import com.arabsoft.communication.Entities.MessageType;
import com.arabsoft.communication.dto.ChatMessageDTO;
import com.arabsoft.communication.service.ChatService;
import com.arabsoft.communication.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ChatWebSocketController {

    private final ChatService chatService;
    private final PresenceService presenceService;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageDTO chatMessage) {
        log.info("Received WebSocket message: from {} to {} type {}",
                chatMessage.getSenderId(), chatMessage.getReceiverId(), chatMessage.getMessageType());
        chatService.processAndSendMessage(chatMessage);
    }

    @MessageMapping("/chat.addUser")
    public void addUser(@Payload ChatMessageDTO chatMessage, SimpMessageHeaderAccessor headerAccessor) {
        String sessionId = headerAccessor.getSessionId();
        String userId = chatMessage.getSenderId();
        String userName = chatMessage.getSenderName();

        if (headerAccessor.getSessionAttributes() != null) {
            headerAccessor.getSessionAttributes().put("userId", userId);
            headerAccessor.getSessionAttributes().put("userName", userName);
        }

        log.info("Registering user WebSocket connection: {} (Session: {})", userId, sessionId);
        presenceService.userConnected(sessionId, userId, userName);

        // Notify public room that user has joined
        chatMessage.setMessageType(MessageType.JOIN);
        chatMessage.setTimestamp(LocalDateTime.now());
        chatService.processAndSendMessage(chatMessage);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        log.info("WebSocket disconnect event received for session: {}", sessionId);
        presenceService.userDisconnected(sessionId);
    }
}
