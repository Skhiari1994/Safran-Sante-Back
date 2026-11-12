package com.arabsoft.communication.service;

import com.arabsoft.communication.dto.UserStatusDTO;

import com.arabsoft.communication.Entities.UserStatus;

import java.time.LocalDateTime;
import java.util.Map;

public interface PresenceService {

    void userConnected(String sessionId, String userId, String userName);

    void userDisconnected(String sessionId);

    boolean isUserOnline(String userId);

    LocalDateTime getLastSeen(String userId);

    /** Tous les statuts enregistrés, en une seule requête (liste des contacts : évite 2 requêtes par utilisateur). */
    Map<String, UserStatus> loadAllStatuses();

    /** Comme {@link #isUserOnline(String)}, mais lit le statut dans la liste déjà chargée. */
    boolean isUserOnline(String userId, Map<String, UserStatus> statuses);

    /** Comme {@link #getLastSeen(String)}, mais lit le statut dans la liste déjà chargée. */
    LocalDateTime getLastSeen(String userId, Map<String, UserStatus> statuses);

    UserStatusDTO getUserStatus(String userId);
}
