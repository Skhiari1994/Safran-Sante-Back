package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultMed;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultVisit;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultVisit;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultVisitProjection;

import java.util.List;

public interface LigBultVisitRepository extends JpaRepository<LigBultVisit, CleLigBultVisit> {

        @Query(value = "select * from lig_bult_visit where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin", nativeQuery = true)
        List<LigBultVisit> getLigBultVisit(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Query(value = "select t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.num_fam,\n" +
                        "       t.dat_soin,\n" +
                        "       t.abrv_act,\n" +
                        "       t.cod_visit,\n" +
                        "       t.num_lig,\n" +
                        "       t.mnt_honor,\n" +
                        "       t.mnt_remb,\n" +
                        "       t.mnt_net,\n" +
                        "       t.indice,\n" +
                        "       t.dat_act,\n" +
                        "       t.prf_typ,\n" +
                        "       t.prf_cod,\n" +
                        "       t.prix_visit,\n" +
                        "       t.taux_remb,\n" +
                        "       t.mut_mnt_net,\n" +
                        "       (select lib_visit from ref_visit r where r.cod_visit=t.cod_visit)lib_visit,\n" +
                        "       (select ETAB_RSOC ||' '|| PR_RSOC  from ref_etablis e where e.prf_typ=t.prf_typ and e.prf_cod=t.prf_cod)lib_etablis from lig_bult_visit t  where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin", nativeQuery = true)
        List<LigBultVisitProjection> getLigBultVisitCons(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "delete from lig_bult_visit where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and dat_act=:datAct and cod_visit=:codVisit", nativeQuery = true)
        void deleteLigBultVisit(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin, @Param("abrv") String abrv, @Param("datAct") String datAct,
                        @Param("codVisit") String codVisit);
}
