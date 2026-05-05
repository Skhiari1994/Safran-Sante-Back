package com.arabsoft.referentiel.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
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
@Table(name = "param_pieces_pret")
@ToString
@SuppressWarnings({ "java:S116" })
public class ParamPiecesPret {

  @Id
  private String cod_piece_pret;

  private String lib_piece_pret;

  private String lib_piece_pret_a;

}
