package com.arabsoft.communication.serviceImpl;

import com.arabsoft.communication.Entities.MessageType;
import com.arabsoft.communication.Entities.UserStatus;
import com.arabsoft.communication.dto.ChatMessageDTO;
import com.arabsoft.communication.dto.UserStatusDTO;
import com.arabsoft.communication.repository.UserStatusRepository;
import com.arabsoft.communication.service.PresenceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class PresenceServiceImpl implements PresenceService {

    private final SimpMessagingTemplate messagingTemplate;
    private final UserStatusRepository userStatusRepository;

    // sessionId -> userId
    private final Map<String, String> sessionUserMap = new ConcurrentHashMap<>();
    // userId -> active session count
    private final Map<String, Integer> userSessionCount = new ConcurrentHashMap<>();
    // userId -> last seen timestamp
    private final Map<String, LocalDateTime> userLastSeen = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public void userConnected(String sessionId, String userId, String userName) {
        if (userId == null || userId.trim().isEmpty()) {
            return;
        }

        sessionUserMap.put(sessionId, userId);
        userSessionCount.merge(userId, 1, Integer::sum);
        LocalDateTime now = LocalDateTime.now();
        userLastSeen.put(userId, now);

        log.info("User connected: {} ({}) - Session: {}", userName, userId, sessionId);

        // Update database table USER_STATUS
        try {
            UserStatus status = userStatusRepository.findById(userId)
                    .orElseGet(() -> UserStatus.builder().userId(userId).build());
            status.setIsOnline(true);
            status.setLastSeen(now);
            userStatusRepository.save(status);
        } catch (Exception e) {
            log.warn("Could not persist online status for user {}: {}", userId, e.getMessage());
        }

        // Broadcast status update on /topic/user-status
        ChatMessageDTO statusMsg = ChatMessageDTO.builder()
                .senderId(userId)
                .senderName(userName)
                .messageType(MessageType.ONLINE)
                .timestamp(now)
                .isOnline(true)
                .lastSeen(now)
                .build();

        messagingTemplate.convertAndSend("/topic/user-status", statusMsg);
    }

    @Override
    @Transactional
    public void userDisconnected(String sessionId) {
        String userId = sessionUserMap.remove(sessionId);
        if (userId != null) {
            Integer count = userSessionCount.computeIfPresent(userId, (k, v) -> v > 1 ? v - 1 : null);
            LocalDateTime now = LocalDateTime.now();
            userLastSeen.put(userId, now);

            if (count == null) {
                log.info("User offline: {}", userId);

                // Update database table USER_STATUS
                try {
                    UserStatus status = userStatusRepository.findById(userId)
                            .orElseGet(() -> UserStatus.builder().userId(userId).build());
                    status.setIsOnline(false);
                    status.setLastSeen(now);
                    userStatusRepository.save(status);
                } catch (Exception e) {
                    log.warn("Could not persist offline status for user {}: {}", userId, e.getMessage());
                }

                ChatMessageDTO statusMsg = ChatMessageDTO.builder()
                        .senderId(userId)
                        .messageType(MessageType.OFFLINE)
                        .timestamp(now)
                        .isOnline(false)
                        .lastSeen(now)
                        .build();

                messagingTemplate.convertAndSend("/topic/user-status", statusMsg);
            }
        }
    }

    @Override
    public boolean isUserOnline(String userId) {
        if (userId == null) {
            return false;
        }
        if (userSessionCount.containsKey(userId)) {
            return true;
        }
        return userStatusRepository.findById(userId)
                .map(status -> Boolean.TRUE.equals(status.getIsOnline()))
                .orElse(false);
    }

    @Override
    public LocalDateTime getLastSeen(String userId) {
        if (userId == null) {
            return null;
        }
        LocalDateTime inMemory = userLastSeen.get(userId);
        if (inMemory != null) {
            return inMemory;
        }
        return userStatusRepository.findById(userId)
                .map(UserStatus::getLastSeen)
                .orElse(null);
    }

    @Override
    public UserStatusDTO getUserStatus(String userId) {
        boolean online = isUserOnline(userId);
        LocalDateTime seen = getLastSeen(userId);
        return UserStatusDTO.builder()
                .userId(userId)
                .isOnline(online)
                .lastSeen(seen)
                .build();
    }
}
