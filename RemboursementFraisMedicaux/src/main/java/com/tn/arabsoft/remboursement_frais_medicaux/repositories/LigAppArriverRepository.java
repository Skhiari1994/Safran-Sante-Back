package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigAppArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigAppArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigAppArriverProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigAppArriverRepository extends JpaRepository<LigAppArriver, LigAppArriverCle> {
        @Query(value = "SELECT lbv.dat_act,\n" +
                        "                   lbv.cod_app,\n" +
                        "                   (SELECT a.lib_app\n" +
                        "                      FROM ref_appareil a\n" +
                        "                     WHERE a.cod_app = lbv.cod_app\n" +
                        "                       AND ROWNUM = 1) AS lib_app,\n" +
                        "                   lbv.mnt_honor,\n" +
                        "                   lbv.mnt_remb,\n" +
                        "                   DECODE(lbv.prf_typ,\n" +
                        "                          1,\n" +
                        "                          'Personne physique',\n" +
                        "                          2,\n" +
                        "                          'Etablissement',\n" +
                        "                          'Autre') AS prf_type,\n" +
                        "                   lbv.prf_typ,\n" +
                        "                   (SELECT re.etab_rsoc || ' ' || re.pr_rsoc\n" +
                        "                    FROM ref_etablis re\n" +
                        "                    WHERE re.prf_cod = lbv.prf_cod\n" +
                        "                    AND ROWNUM = 1) AS lib_org,\n" +
                        "                   lbv.prf_cod,\n" +
                        "                   lbv.accord_app,lbv.dat_soin,lbv.cod_soc,lbv.num_fam,lbv.mat_pers,lbv.abrv_act, lbv.num_pec_app, lbv.num_lig\n"
                        +
                        "              FROM lig_app_arriver lbv WHERE cod_soc = :cod_soc AND mat_pers = :mat_pers AND num_fam = :num_fam AND dat_soin = to_date(:dat_soin,'dd/mm/yyyy')", nativeQuery = true)
        List<LigAppArriverProjection> findLigAppArriverById(@Param("cod_soc") String codSoc,
                        @Param("mat_pers") String matPers,
                        @Param("num_fam") Integer numFam,
                        @Param("dat_soin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "delete from lig_app_arriver where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and num_lig=:numLig and cod_app=:codApp", nativeQuery = true)
        void deleteLigAppArriver(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin, @Param("abrv") String abrv, @Param("numLig") String numLig,
                        @Param("codApp") String codApp);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM lig_app_arriver WHERE cod_soc = :codSoc AND mat_pers = :matPers AND num_fam = :numFam AND dat_soin = to_date(:datSoin, 'dd/mm/yyyy')", nativeQuery = true)
        void deleteByBulletin(@Param("codSoc") String codSoc, @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam, @Param("datSoin") String datSoin);

        @Query("SELECT MAX(l.num_lig) FROM LigMedArriver l WHERE l.cod_soc = :codSoc AND l.mat_pers = :matPers AND l.num_fam = :numFam AND l.dat_soin = :datSoin")
        Integer findMaxNumLig(String codSoc, String matPers, Integer numFam, java.time.LocalDate datSoin);

}
