package com.arabsoft.communication.service;

import com.arabsoft.communication.dto.ChatMessageDTO;
import com.arabsoft.communication.dto.ChatRoomDTO;
import com.arabsoft.communication.dto.UserInfoDTO;

import java.util.List;

public interface ChatService {

    ChatMessageDTO processAndSendMessage(ChatMessageDTO messageDTO);

    String getOrCreateRoom(String user1Id, String user2Id);

    List<ChatMessageDTO> getRoomMessages(String roomId);

    List<ChatRoomDTO> getUserChatRooms(String userId);

    List<ChatMessageDTO> getRecentConversations(String userId);

    void markRoomAsRead(String roomId, String userId);

    void markMessageAsRead(Long messageId);

    int getUnreadCount(String userId);

    List<ChatMessageDTO> getUnreadMessages(String userId);

    void deleteRoomMessages(String roomId);

    List<UserInfoDTO> getAllUsers();

    String getAdherentDisplayName(String userId);
}
