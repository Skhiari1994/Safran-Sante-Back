package com.arabsoft.communication.Entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "USER_STATUS")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserStatus {

    @Id
    @Column(name = "USER_ID", length = 100, nullable = false)
    private String userId;

    @Column(name = "IS_ONLINE")
    private Boolean isOnline;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "LAST_SEEN")
    private LocalDateTime lastSeen;
}
