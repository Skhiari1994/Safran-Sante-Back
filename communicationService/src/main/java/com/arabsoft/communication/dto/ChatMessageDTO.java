package com.arabsoft.communication.dto;

import com.arabsoft.communication.Entities.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessageDTO {

    private Long id;
    private String senderId;
    private String senderName;
    private String receiverId;
    private String roomId;
    private String content;
    private MessageType messageType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Builder.Default
    private Boolean isRead = false;

    private Integer unreadCount;
    private Boolean isOnline;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastSeen;
}
