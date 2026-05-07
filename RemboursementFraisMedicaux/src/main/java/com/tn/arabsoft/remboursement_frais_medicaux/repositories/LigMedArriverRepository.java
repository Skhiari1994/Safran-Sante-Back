package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigMedArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigMedArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigMedArriverProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigMedArriverRepository extends JpaRepository<LigMedArriver, LigMedArriverCle> {

        @Query(value = """
                        select
                            lbv.dat_act,
                            lbv.cod_med,
                            lbv.abrv_act,
                            rm.lib_med,
                            lbv.indice,
                            lbv.mnt_honor,
                            lbv.mnt_remb,
                            lbv.mut_mnt_net,
                            case lbv.prf_typ
                                when 1 then 'Personne physique'
                                when 2 then 'Etablissement'
                                else 'Autre'
                            end as prf_type,
                            lbv.prf_typ,
                            concat(
                                coalesce(re.etab_rsoc, ''),
                                concat(' ', coalesce(re.pr_rsoc, ''))
                            ) as lib_org,
                            lbv.prf_cod,
                            lbv.dat_soin,
                            lbv.cod_soc,
                            lbv.num_fam,
                            lbv.mat_pers,
                            lbv.num_lig,
                            lbv.accord_med,
                            lbv.nbr_j,
                            lbv.num_pec_med
                        from lig_med_arriver lbv
                        left join ref_med rm
                               on rm.cod_med = lbv.cod_med
                        left join ref_etablis re
                               on re.prf_cod = lbv.prf_cod
                        where lbv.cod_soc = :cod_soc
                          and lbv.mat_pers = :mat_pers
                          and lbv.num_fam = :num_fam
                          and lbv.dat_soin = :dat_soin
                        """, nativeQuery = true)
        List<LigMedArriverProjection> findLigMedArriverById(
                        @Param("cod_soc") String codSoc,
                        @Param("mat_pers") String matPers,
                        @Param("num_fam") Integer numFam,
                        @Param("dat_soin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        DELETE FROM lig_med_arriver
                         WHERE cod_soc = :soc
                           AND mat_pers = :mat
                           AND num_fam = :fam
                           AND dat_soin = :datSoin
                           AND abrv_act = :abrv
                           AND cod_med = :med
                           AND num_lig = :numLig
                        """, nativeQuery = true)
        void deleteMedArriver(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") Integer fam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("abrv") String abrv,
                        @Param("med") String med,
                        @Param("numLig") Long numLig);

        @Modifying
        @Transactional
        @Query(value = """
                        DELETE FROM lig_med_arriver
                         WHERE cod_soc = :codSoc
                           AND mat_pers = :matPers
                           AND num_fam = :numFam
                           AND dat_soin = :datSoin
                        """, nativeQuery = true)
        void deleteByBulletin(
                        @Param("codSoc") String codSoc,
                        @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Query("""
                        SELECT MAX(l.num_lig)
                          FROM LigMedArriver l
                         WHERE l.cod_soc = :codSoc
                           AND l.mat_pers = :matPers
                           AND l.num_fam = :numFam
                           AND l.dat_soin = :datSoin
                        """)
        Integer findMaxNumLig(
                        @Param("codSoc") String codSoc,
                        @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);
}