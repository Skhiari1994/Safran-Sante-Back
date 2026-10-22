package com.arabsoft.communication.mapper;

import com.arabsoft.communication.Entities.ChatMessage;
import com.arabsoft.communication.Entities.ChatRoom;
import com.arabsoft.communication.dto.ChatMessageDTO;
import com.arabsoft.communication.dto.ChatRoomDTO;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public final class ChatMapper {

    private ChatMapper() {
    }

    public static ChatMessageDTO toDTO(ChatMessage entity) {
        if (entity == null) {
            return null;
        }
        return ChatMessageDTO.builder()
                .id(entity.getId())
                .senderId(entity.getSenderId())
                .senderName(entity.getSenderName())
                .receiverId(entity.getReceiverId())
                .roomId(entity.getRoomId())
                .content(entity.getContent())
                .messageType(entity.getMessageType())
                .timestamp(entity.getTimestamp())
                .isRead(entity.getIsRead() != null ? entity.getIsRead() : false)

                .readAt(entity.getReadAt())
                .replyToId(entity.getReplyToId())
                .build();
    }

    public static ChatMessage toEntity(ChatMessageDTO dto) {
        if (dto == null) {
            return null;
        }
        return ChatMessage.builder()
                .id(dto.getId())
                .senderId(dto.getSenderId())
                .senderName(dto.getSenderName())
                .receiverId(dto.getReceiverId())
                .roomId(dto.getRoomId())
                .content(dto.getContent())
                .messageType(dto.getMessageType())
                .timestamp(dto.getTimestamp())
                .isRead(dto.getIsRead() != null ? dto.getIsRead() : false)

                .replyToId(dto.getReplyToId())
                .build();
    }

    public static List<ChatMessageDTO> toDTOList(List<ChatMessage> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        return entities.stream().map(ChatMapper::toDTO).collect(Collectors.toList());
    }

    public static ChatRoomDTO toRoomDTO(ChatRoom room) {
        if (room == null) {
            return null;
        }
        return ChatRoomDTO.builder()
                .id(room.getId())
                .roomId(room.getRoomId())
                .user1Id(room.getUser1Id())
                .user2Id(room.getUser2Id())
                .createdAt(room.getCreatedAt())
                .lastMessageAt(room.getLastMessageAt())
                .build();
    }

    public static List<ChatRoomDTO> toRoomDTOList(List<ChatRoom> rooms) {
        if (rooms == null) {
            return Collections.emptyList();
        }
        return rooms.stream().map(ChatMapper::toRoomDTO).collect(Collectors.toList());
    }
}
