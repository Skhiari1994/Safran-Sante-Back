package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.LigBultCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigBult;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigBultProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LigBultRepository extends JpaRepository<LigBult, LigBultCle> {

    @Query(value="select * from lig_bult where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin",nativeQuery = true)
    List<LigBult> getLigBult(@Param("soc")String soc, @Param("mat")String mat, @Param("numFam")String numFam, @Param("datSoin")String datSoin);

    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_fam,\n" +
            "       t.dat_soin,\n" +
            "       t.abrv_act,\n" +
            "       t.num_lig,\n" +
            "       t.prf_typ,\n" +
            "       t.prf_cod,\n" +
            "       t.dat_act,\n" +
            "       t.indice,\n" +
            "       t.mnt_honor,\n" +
            "       t.mnt_net,\n" +
            "       t.mnt_remb,\n" +
            "       t.obs,\n" +
            "       t.obs_a,\n" +
            "       t.nbr_piece,\n" +
            "       t.nbr_vign,\n" +
            "       t.nat_act,\n" +
            "       t.mtt_acte,\n" +
            "       t.taux_act,\n" +
            "       t.plafonne,\n" +
            "       t.plafond,\n" +
            "       t.a_indice,\n" +
            "       t.ctr_duree,\n" +
            "       t.duree_act,\n" +
            "       t.imput_plaf,\n" +
            "       (select lib_act from acte a where a.abrv_act=t.abrv_act)lib_act,\n" +
            "       (select e.etab_rsoc from etabliss e where e.prf_typ=t.prf_typ and e.prf_cod=t.prf_cod)lib_etablis from lig_bult t" +
            " where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin",nativeQuery = true)
    List<LigBultProjection> getLigBultCons(@Param("soc")String soc, @Param("mat")String mat, @Param("numFam")String numFam, @Param("datSoin")String datSoin);


    @Modifying
    @Transactional
    @Query(value="delete from lig_bult where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and num_lig=:numLig",nativeQuery = true)
    void deleteLigBult(@Param("soc")String soc,@Param("mat")String mat,@Param("fam")String fam,@Param("datSoin")String datSoin,@Param("abrv")String abrv,@Param("numLig")String numLig);



}
