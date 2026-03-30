package com.arabsoft.referentiel.Entities;


import com.arabsoft.referentiel.Entities.Cle.ClePiecePret;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
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
@IdClass(ClePiecePret.class)
public class PiecesPret {
     @Id
    private String  cod_soc;
    @Id
    private String   cod_grp_pret;
    @Id
    private String   typ_pret;
    @Id
    private String   cod_piece_pret	;
    private String  nat_piece;
    private String  oblig_piece	;
    private String  nbr_copie;

}
