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
public class NatureDon {

    @Id
    public String nat_don;
    public String lib_nat_don;
    public String lib_nat_don_a;
    public String typ_don;
    public String debut_cycle;
    public String fin_cycle;
    public Long frequence;
    public String limit_depot;
    public String cod_fond;
    public Long max_mnt_don;
    public String mnt_variable;
    public String concerne;
    public Long min_mnt_don;
    public String cod_benif;
    public Long diff_period;
    public String compt_charge;



}
