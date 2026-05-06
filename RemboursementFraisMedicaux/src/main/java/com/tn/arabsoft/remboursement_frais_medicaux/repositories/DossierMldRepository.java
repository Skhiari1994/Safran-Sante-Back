package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.DossierMld;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleDossierMld;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.DossierMldProjection;

import java.util.List;

public interface DossierMldRepository extends JpaRepository<DossierMld, CleDossierMld> {

  @Query(value = """
      select t.cod_soc,
             t.mat_pers,
             t.num_dos_mld,
             t.cod_malad,
             t.dat_doss_mld,
             t.etat_dos_mld,
             t.num_fam,
             (select lib_malad
              from maladie
              where cod_malad = t.cod_malad) as libMalad,
             case
               when t.num_fam <> 0 then (
                 select nom_pren
                 from famille
                 where cod_soc = t.cod_soc
                   and mat_pers = t.mat_pers
                   and num_fam = t.num_fam
               )
               else 'Adhérent'
             end as nom
      from dossier_mld t
      where t.cod_soc = :soc
        and t.mat_pers = :mat
      order by t.dat_doss_mld desc
      """, nativeQuery = true)
  List<DossierMldProjection> getDossierMll(@Param("soc") String soc, @Param("mat") String mat);

  @Query(value = """
      select coalesce(max(num_dos_mld), 0) + 1
      from dossier_mld
      where cod_soc = :soc
        and mat_pers = :mat
      """, nativeQuery = true)
  Long getNumDoss(@Param("soc") String soc, @Param("mat") String mat);

}