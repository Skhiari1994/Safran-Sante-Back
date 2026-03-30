package com.arabsoft.Credits.Entities;

import com.arabsoft.Credits.Configuration.CustomLocalDateDeserializer;
import com.arabsoft.Credits.Entities.Cles.CleDetailsCommission;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
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
@IdClass(CleDetailsCommission.class)
public class DetailsCommission {
    @Id
    private String cod_soc;
    @Id
    private Long num_comm;
    @Id
    private String mat_pers;
    @Id
    private Long  num_dem_pret;
    private String resultat_comm;
    private Long mnt_acc;
    @JsonDeserialize(using = CustomLocalDateDeserializer.class)
    private LocalDate dat_effet;
    private Long   nbr_ech_acc;
    private Long delai_grace;
    private Long nbr_tranche;
    private String cod_rejet;
    private String cod_etat_pret;
    private String typ_etat;
    private String  obs_comm;

}
