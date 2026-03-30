package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Entities.Cles.ClePersonnel;
import com.arabsoft.Credits.Entities.Cles.ClePiecePRetPers;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(ClePiecePRetPers.class)
public class PiecePretPers {
 @Id
   private String cod_soc;
 @Id
    private String  mat_pers;
 @Id
    private Long cod_pret;
 @Id
    private String  cod_piece_pret;
    private Long  nbr_copie;
    private LocalDate dat_piece;
    private String  obs_piece;

}
