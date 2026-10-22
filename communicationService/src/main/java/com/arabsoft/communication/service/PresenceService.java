package com.arabsoft.communication.service;

import com.arabsoft.communication.dto.UserStatusDTO;

import java.time.LocalDateTime;

public interface PresenceService {

    void userConnected(String sessionId, String userId, String userName);

    void userDisconnected(String sessionId);

    boolean isUserOnline(String userId);

    LocalDateTime getLastSeen(String userId);

    UserStatusDTO getUserStatus(String userId);
}
