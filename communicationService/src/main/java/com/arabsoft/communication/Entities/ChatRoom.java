package com.arabsoft.communication.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_ROOM")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_chat_room")
    @SequenceGenerator(name = "seq_chat_room", sequenceName = "SEQ_CHAT_ROOM", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ROOM_ID", length = 100, unique = true, nullable = false)
    private String roomId;

    @Column(name = "USER1_ID", length = 100, nullable = false)
    private String user1Id;

    @Column(name = "USER2_ID", length = 100, nullable = false)
    private String user2Id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "LAST_MESSAGE_AT")
    private LocalDateTime lastMessageAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.lastMessageAt == null) {
            this.lastMessageAt = LocalDateTime.now();
        }
    }
}
