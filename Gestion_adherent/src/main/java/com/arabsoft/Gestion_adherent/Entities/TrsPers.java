package com.arabsoft.Gestion_adherent.Entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class TrsPers {

    private String cin	;
    @Id

    private String  mat;
    private String  retr;
    private String  nom	;
    private String  x5;
    private String x6;
    private String  tel	;
    private String  typ	;

}
