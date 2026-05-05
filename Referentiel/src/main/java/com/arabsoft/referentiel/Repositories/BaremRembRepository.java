package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.BaremeRemb;
import com.arabsoft.referentiel.entities.cle.BaremRembCle;
import com.arabsoft.referentiel.projections.BaremeRembProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaremRembRepository extends JpaRepository<BaremeRemb, BaremRembCle> {

  @Query(value = """
      select t.cod_fil,
             t.abrv_act,
             t.cod_assur,
             t.a_indice,
             t.mtt_acte,
             t.dat_acte,
             t.taux_act,
             t.plafonne,
             t.plafond,
             t.verif_piece,
             t.nat_act,
             t.verif_vign,
             t.duree_act,
             t.plafon_prest,
             (select a.lib_act
                from acte a
               where a.abrv_act = t.abrv_act) as lib_act
        from bareme_remb t
      """, nativeQuery = true)
  List<BaremeRembProjection> getAllBareme();

}
