package com.arabsoft.referentiel.Entities;


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
public class ParamPiecesPret {
    @Id
  private String  cod_piece_pret;
  private String   lib_piece_pret;
  private String   lib_piece_pret_a;

}
