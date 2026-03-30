package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.BaremeRemb;
import com.arabsoft.referentiel.Entities.Cle.BaremRembCle;
 import com.arabsoft.referentiel.Projections.BaremeRembProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BaremRembRepository extends JpaRepository<BaremeRemb, BaremRembCle> {

    @Query(value="select t.cod_fil,\n" +
            "       t.abrv_act,\n" +
            "       t.cod_assur,\n" +
            "       t.a_indice,\n" +
            "       t.mtt_acte,\n" +
            "       t.dat_acte,\n" +
            "       t.taux_act,\n" +
            "       t.plafonne,\n" +
            "       t.plafond,\n" +
            "       t.verif_piece,\n" +
            "       t.nat_act,\n" +
            "       t.verif_vign,\n" +
            "       t.duree_act,\n" +
            "       t.plafon_prest,\n" +
            "       (select a.lib_act from acte a where a.abrv_act=t.abrv_act) as lib_act\n" +
            "from Bareme_Remb t  ",nativeQuery = true)
    List<BaremeRembProjection> getAllBareme();

}
