package com.arabsoft.communication.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private String id;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean isOnline;
    private String lastSeen;
}
