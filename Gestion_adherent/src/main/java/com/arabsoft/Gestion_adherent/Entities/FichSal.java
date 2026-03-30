package com.arabsoft.Gestion_adherent.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@ToString
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class FichSal {

    @Id
    private Long ordre;
    private String  ligne;
    private String typ_lig;
}
