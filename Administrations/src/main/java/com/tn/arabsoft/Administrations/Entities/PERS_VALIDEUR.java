package com.tn.arabsoft.administrations.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@IdClass(Cle_PERS_VALIDEUR.class)
@SuppressWarnings({ "java:S101", "java:S116" })
public class PERS_VALIDEUR {

    @Id
    private String cod_soc;

    @Id
    private String mat_pers;

    @Id
    private String mat_resp;

    private Long niveau;

}
