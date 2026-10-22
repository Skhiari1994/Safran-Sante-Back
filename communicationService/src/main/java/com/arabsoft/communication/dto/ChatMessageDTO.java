package com.arabsoft.communication.dto;

import com.arabsoft.communication.Entities.MessageType;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.*;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

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

    // l'heure est fixée par le serveur : celle envoyée par le client (ISO avec « Z ») est ignorée
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    @Builder.Default
    private Boolean isRead = false;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime readAt;

    private Long replyToId;

    // réactions (émojis) des participants ; renseignées par le serveur uniquement
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<ReactionDTO> reactions;

    private Integer unreadCount;
    private Boolean isOnline;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastSeen;
}
