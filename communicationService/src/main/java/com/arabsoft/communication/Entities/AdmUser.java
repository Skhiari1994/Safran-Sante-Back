package com.arabsoft.communication.Entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admuser")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdmUser {

    @Id
    @Column(name = "USE_ID")
    private Long useId;

    @Column(name = "USE_MATRICULE")
    private String matricule;

    @Column(name = "USE_LOGIN")
    private String login;

    @Column(name = "USE_FNAME")
    private String firstName;

    @Column(name = "USE_LNAME")
    private String lastName;
}
