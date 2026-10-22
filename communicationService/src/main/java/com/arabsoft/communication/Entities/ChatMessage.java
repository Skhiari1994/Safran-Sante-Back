package com.arabsoft.communication.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_MESSAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_msg_seq")
    @SequenceGenerator(name = "chat_msg_seq", sequenceName = "CHAT_MSG_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SENDER_ID", length = 100, nullable = false)
    private String senderId;

    @Column(name = "SENDER_NAME", length = 255)
    private String senderName;

    @Column(name = "RECEIVER_ID", length = 100)
    private String receiverId;

    @Column(name = "ROOM_ID", length = 100)
    private String roomId;

    @Lob
    @Column(name = "CONTENT", columnDefinition = "CLOB")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "MESSAGE_TYPE", length = 30)
    private MessageType messageType;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "TIMESTAMP")
    private LocalDateTime timestamp;

    @Builder.Default
    @Column(name = "IS_READ")
    private Boolean isRead = false;

    @PrePersist
    public void prePersist() {
        if (this.timestamp == null) {
            this.timestamp = LocalDateTime.now();
        }
        if (this.isRead == null) {
            this.isRead = false;
        }
    }
}
