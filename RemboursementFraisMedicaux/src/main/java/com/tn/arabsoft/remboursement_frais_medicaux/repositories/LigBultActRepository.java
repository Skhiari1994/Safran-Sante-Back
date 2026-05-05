package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBult;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultAct;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultAct;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultActProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigBultActRepository extends JpaRepository<LigBultAct, CleLigBultAct> {

        @Query(value = "select nvl(max(nvl(num_lig,0)),0) + 1  \n" +
                        "from lig_bult_act \n" +
                        "where COD_SOC = :soc \n" +
                        "and mat_pers = :mat \n" +
                        "and num_fam = :numFam \n" +
                        "and dat_soin = :datSoin", nativeQuery = true)
        Long getNumLigAct(@Param("soc") String soc, @Param("mat") String mat, @Param("numFam") Long numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Query(value = "select * from lig_bult_act where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin", nativeQuery = true)
        List<LigBultAct> getLigBultAct(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Query(value = "select t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.num_fam,\n" +
                        "       t.dat_soin,\n" +
                        "       t.abrv_act,\n" +
                        "       t.cod_act,\n" +
                        "       t.num_lig,\n" +
                        "       t.let_cod,\n" +
                        "       t.cot_act,\n" +
                        "       t.act_prix,\n" +
                        "       t.mnt_honor,\n" +
                        "       t.mnt_remb,\n" +
                        "       t.accord_act,\n" +
                        "       t.mnt_net,\n" +
                        "       t.indice,\n" +
                        "       t.dat_act,\n" +
                        "       t.prf_typ,\n" +
                        "       t.prf_cod,\n" +
                        "       t.mut_mnt_net,\n" +
                        "       t.num_pec_act,\n" +
                        "       t.taux_act,\n" +
                        "       t.typ_prf,\n" +
                        "       (select lib_act from ref_act r where r.cod_act=t.cod_act)lib_act,\n" +
                        "       (select ETAB_RSOC ||' '|| PR_RSOC  from ref_etablis e where e.prf_typ=t.prf_typ and e.prf_cod=t.prf_cod)lib_etablis from lig_bult_act t where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin", nativeQuery = true)
        List<LigBultActProjection> getLigBultActCons(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "delete from lig_bult_act where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and num_lig=:numLig and dat_act=:datAct and cod_act=:codAct", nativeQuery = true)
        void deleteLigBultAct(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin, @Param("abrv") String abrv, @Param("numLig") String numLig,
                        @Param("datAct") String datAct, @Param("codAct") String codAct);
}
