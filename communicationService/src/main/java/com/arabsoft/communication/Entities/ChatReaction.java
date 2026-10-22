package com.arabsoft.communication.Entities;

import jakarta.persistence.*;
import lombok.*;

/** Réaction (émoji) d'un utilisateur sur un message : une seule réaction par utilisateur et par message. */
@Entity
@Table(name = "CHAT_REACTION", uniqueConstraints = @UniqueConstraint(columnNames = {"MESSAGE_ID", "USER_ID"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatReaction {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_react_seq")
    @SequenceGenerator(name = "chat_react_seq", sequenceName = "CHAT_REACT_SEQ", allocationSize = 1)
    @Column(name = "ID")
    private Long id;

    @Column(name = "MESSAGE_ID", nullable = false)
    private Long messageId;

    @Column(name = "USER_ID", length = 100, nullable = false)
    private String userId;

    @Column(name = "EMOJI", length = 20, nullable = false)
    private String emoji;
}
