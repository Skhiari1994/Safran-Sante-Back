package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultMed;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultMed;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultMedProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigBultMedRepository extends JpaRepository<LigBultMed, CleLigBultMed> {

        @Query(value = """
                        select coalesce(max(coalesce(num_lig_med, 0)), 0) + 1
                          from lig_bult_med
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                           and cod_med = :codMed
                        """, nativeQuery = true)
        Long getNumLigMed(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Long numFam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("codMed") String codMed);

        @Query(value = """
                        select *
                          from lig_bult_med
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultMed> getLigBultMed(
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
                            t.cod_med,
                            t.num_lig,
                            t.indice,
                            t.mnt_honor,
                            t.mnt_net,
                            t.mnt_remb,
                            t.accord_med,
                            t.dat_act,
                            t.prf_typ,
                            t.prf_cod,
                            t.num_pec_med,
                            t.mdc_prix,
                            t.med_prix,
                            t.prix_remb,
                            t.num_lig_med,
                            t.mut_mnt_net,
                            t.nbr_j,
                            r.lib_med,
                            concat(coalesce(e.etab_rsoc, ''), concat(' ', coalesce(e.pr_rsoc, ''))) as lib_etablis
                        from lig_bult_med t
                        left join ref_med r
                               on r.cod_med = t.cod_med
                        left join ref_etablis e
                               on e.prf_typ = t.prf_typ
                              and e.prf_cod = t.prf_cod
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                          and t.num_fam = :numFam
                          and t.dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultMedProjection> getLigBultMedCons(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_bult_med
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :fam
                           and dat_soin = :datSoin
                           and abrv_act = :abrv
                           and num_lig_med = :numLig
                           and cod_med = :codMed
                        """, nativeQuery = true)
        void deleteLigBultMed(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") Integer fam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("abrv") String abrv,
                        @Param("numLig") Long numLig,
                        @Param("codMed") String codMed);

}