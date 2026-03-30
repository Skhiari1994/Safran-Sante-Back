package com.arabsoft.auth.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seqUser")
    @SequenceGenerator(name = "seqUser", sequenceName = "SEQ_TOKEN", allocationSize = 1)
    private Integer id ;
    @Column(name="token")
    private String token ;
    @Column(name="created_at")

    private LocalDateTime createdAt ;
    @Column(name="expires_at")
    private LocalDateTime expiresAt;
    @Column(name="validated_at")
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name="userid" , nullable = false)
    private User user;

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", createdAt=" + createdAt +
                ", expiresAt=" + expiresAt +
                ", validatedAt=" + validatedAt +
                ", user=" + user +
                '}';
    }
}
