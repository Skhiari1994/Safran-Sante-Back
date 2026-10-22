package com.arabsoft.communication.repository;

import com.arabsoft.communication.Entities.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByRoomIdOrderByTimestampAsc(String roomId);

    List<ChatMessage> findByReceiverIdAndIsReadFalse(String receiverId);

    int countByReceiverIdAndIsReadFalse(String receiverId);

    int countByRoomIdAndReceiverIdAndIsReadFalse(String roomId, String receiverId);

    @Transactional
    void deleteByRoomId(String roomId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage m SET m.isRead = true, m.readAt = :now WHERE m.roomId = :roomId AND m.receiverId = :receiverId AND m.isRead = false")
    int markRoomMessagesAsRead(@Param("roomId") String roomId, @Param("receiverId") String receiverId, @Param("now") LocalDateTime now);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage m SET m.isRead = true, m.readAt = :now WHERE m.id = :id AND m.isRead = false")
    int markMessageAsRead(@Param("id") Long id, @Param("now") LocalDateTime now);

    List<ChatMessage> findByRoomIdOrderByTimestampDesc(String roomId);

    default ChatMessage findLatestMessageByRoomId(String roomId) {
        List<ChatMessage> list = findByRoomIdOrderByTimestampDesc(roomId);
        return (list != null && !list.isEmpty()) ? list.get(0) : null;
    }
}
