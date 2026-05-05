package com.arabsoft.referentiel.entities;

import com.arabsoft.referentiel.entities.cle.ClePiecePret;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pieces_pret")
@ToString
@IdClass(ClePiecePret.class)
@SuppressWarnings({ "java:S116" })
public class PiecesPret {

    @Id
    private String cod_soc;

    @Id
    private String cod_grp_pret;

    @Id
    private String typ_pret;

    @Id
    private String cod_piece_pret;

    private String nat_piece;

    private String oblig_piece;

    private String nbr_copie;

}
