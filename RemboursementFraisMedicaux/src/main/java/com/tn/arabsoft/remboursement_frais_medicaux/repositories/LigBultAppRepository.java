package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBult;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultApp;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultApp;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultAppProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigBultAppRepository extends JpaRepository<LigBultApp, CleLigBultApp> {

        @Query(value = "select nvl(max(nvl(num_lig_app,0)),0) + 1  \n" +
                        "from lig_bult_app\n" +
                        "where COD_SOC = :soc \n" +
                        "and MAT_PERS = :mat \n" +
                        "and NUM_FAM = :numFam \n" +
                        "and DAT_SOIN = :datSoin \n" +
                        "and COD_app = :codApp", nativeQuery = true)
        Long getNumLigApp(@Param("soc") String soc, @Param("mat") String mat, @Param("numFam") Long numFam,
                        @Param("datSoin") LocalDate datSoin, @Param("codApp") String codApp);

        @Query(value = "select * from lig_bult_app where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin", nativeQuery = true)
        List<LigBultApp> getLigBultApp(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Query(value = "select t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.num_fam,\n" +
                        "       t.dat_soin,\n" +
                        "       t.abrv_act,\n" +
                        "       t.cod_app,\n" +
                        "       t.num_lig,\n" +
                        "       t.mnt_honor,\n" +
                        "       t.mnt_net,\n" +
                        "       t.mnt_remb,\n" +
                        "       t.accord_app,\n" +
                        "       t.indice,\n" +
                        "       t.dat_act,\n" +
                        "       t.prf_typ,\n" +
                        "       t.prf_cod,\n" +
                        "       t.num_pec_app,\n" +
                        "       t.num_lig_app,\n" +
                        "       t.mut_mnt_net,\n" +
                        "       (select r.lib_app from ref_appareil r where r.cod_app=t.cod_app)lib_app," +
                        " (select ETAB_RSOC ||' '|| PR_RSOC  from ref_etablis e where e.prf_typ=t.prf_typ and e.prf_cod=t.prf_cod)lib_etablis"
                        +
                        " from lig_bult_app t where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin", nativeQuery = true)
        List<LigBultAppProjection> getLigBultAppCons(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "delete from lig_bult_app where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and num_lig_app=:numLig and cod_app=:codApp", nativeQuery = true)
        void deleteLigBultApp(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin, @Param("abrv") String abrv, @Param("numLig") String numLig,
                        @Param("codApp") String codApp);
}
