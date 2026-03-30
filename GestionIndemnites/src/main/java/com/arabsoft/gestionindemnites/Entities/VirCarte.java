package com.arabsoft.gestionindemnites.Entities;

import jakarta.persistence.*;
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
@Table(name = "VIR_CARTE_DATA")
public class VirCarte {
    @Id
    private Long ordre;
    private String  ligne;
    private String typ_lig;

}
