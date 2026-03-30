package com.tn.arabsoft.Administrations.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.*;
import lombok.experimental.SuperBuilder;

@ToString
@Setter
@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(Cle_PERS_VALIDEUR.class)
public class PERS_VALIDEUR {

    @Id
    private String cod_soc  ;
    @Id
    private String  mat_pers ;
    @Id
    private String  mat_resp ;
    private Long  niveau   ;

}
