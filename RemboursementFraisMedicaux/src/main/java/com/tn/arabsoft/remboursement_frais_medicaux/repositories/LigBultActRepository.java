package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultAct;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultAct;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultActProjection;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings({ "java:S107" })
public interface LigBultActRepository extends JpaRepository<LigBultAct, CleLigBultAct> {

        @Query(value = """
                        select coalesce(max(coalesce(num_lig, 0)), 0) + 1
                        from lig_bult_act
                        where cod_soc = :soc
                          and mat_pers = :mat
                          and num_fam = :numFam
                          and dat_soin = :datSoin
                        """, nativeQuery = true)
        Long getNumLigAct(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Long numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Query(value = "select * from lig_bult_act where cod_soc = :soc and mat_pers = :mat and num_fam = :numFam and dat_soin = :datSoin", nativeQuery = true)
        List<LigBultAct> getLigBultAct(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") Long numFam, @Param("datSoin") LocalDate datSoin);

        @Query(value = """
                        select t.cod_soc,
                               t.mat_pers,
                               t.num_fam,
                               t.dat_soin,
                               t.abrv_act,
                               t.cod_act,
                               t.num_lig,
                               t.let_cod,
                               t.cot_act,
                               t.act_prix,
                               t.mnt_honor,
                               t.mnt_remb,
                               t.accord_act,
                               t.mnt_net,
                               t.indice,
                               t.dat_act,
                               t.prf_typ,
                               t.prf_cod,
                               t.mut_mnt_net,
                               t.num_pec_act,
                               t.taux_act,
                               t.typ_prf,
                               r.lib_act,
                               concat(e.etab_rsoc, concat(' ', e.pr_rsoc)) as lib_etablis
                        from lig_bult_act t
                        left join ref_act r
                               on r.cod_act = t.cod_act
                        left join ref_etablis e
                               on e.prf_typ = t.prf_typ
                              and e.prf_cod = t.prf_cod
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                          and t.num_fam = :numFam
                          and t.dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultActProjection> getLigBultActCons(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Long numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_bult_act
                        where cod_soc = :soc
                        and mat_pers = :mat
                        and num_fam = :numFam
                        and dat_soin = :datSoin
                        and abrv_act = :abrv
                        and num_lig = :numLig
                        and dat_act = :datAct
                        and cod_act = :codAct
                        """, nativeQuery = true)
        void deleteLigBultAct(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Long numFam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("abrv") String abrv,
                        @Param("numLig") Long numLig,
                        @Param("datAct") LocalDate datAct,
                        @Param("codAct") String codAct);
}
