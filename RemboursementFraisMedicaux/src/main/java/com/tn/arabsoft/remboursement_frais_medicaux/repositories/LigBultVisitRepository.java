package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultVisit;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultVisit;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultVisitProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigBultVisitRepository extends JpaRepository<LigBultVisit, CleLigBultVisit> {

        @Query(value = """
                        select *
                          from lig_bult_visit
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultVisit> getLigBultVisit(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Query(value = """
                        select
                            t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.abrv_act,
                            t.cod_visit,
                            t.num_lig,
                            t.mnt_honor,
                            t.mnt_remb,
                            t.mnt_net,
                            t.indice,
                            t.dat_act,
                            t.prf_typ,
                            t.prf_cod,
                            t.prix_visit,
                            t.taux_remb,
                            t.mut_mnt_net,
                            r.lib_visit,
                            concat(coalesce(e.etab_rsoc, ''), concat(' ', coalesce(e.pr_rsoc, ''))) as lib_etablis
                        from lig_bult_visit t
                        left join ref_visit r
                               on r.cod_visit = t.cod_visit
                        left join ref_etablis e
                               on e.prf_typ = t.prf_typ
                              and e.prf_cod = t.prf_cod
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                          and t.num_fam = :numFam
                          and t.dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultVisitProjection> getLigBultVisitCons(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_bult_visit
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :fam
                           and dat_soin = :datSoin
                           and abrv_act = :abrv
                           and dat_act = :datAct
                           and cod_visit = :codVisit
                        """, nativeQuery = true)
        void deleteLigBultVisit(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") Integer fam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("abrv") String abrv,
                        @Param("datAct") LocalDate datAct,
                        @Param("codVisit") String codVisit);

}