package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigVisitArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigVisitArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigVisitArriverProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigVisitArriverRepository extends JpaRepository<LigVisitArriver, LigVisitArriverCle> {

        @Query(value = "SELECT lbv.dat_act,\n" +
                        "       lbv.abrv_act,\n" +
                        "       (SELECT a.lib_act \n" +
                        "        FROM acte a \n" +
                        "        WHERE a.abrv_act = lbv.abrv_act\n" +
                        "        AND ROWNUM = 1) AS lib_act,  \n" +
                        "       lbv.indice,\n" +
                        "       lbv.mnt_honor,\n" +
                        "       lbv.mnt_net,\n" +
                        "       lbv.mnt_remb,\n" +
                        "       DECODE(lbv.prf_typ, \n" +
                        "              1, 'Personne physique', \n" +
                        "              2, 'Etablissement', \n" +
                        "              'Autre') AS prf_type,   \n" +
                        "       lbv.prf_typ,\n" +
                        "       (SELECT re.etab_rsoc || ' ' || re.pr_rsoc \n" +
                        "        FROM ref_etablis re \n" +
                        "        WHERE re.prf_cod = lbv.prf_cod\n" +
                        "        AND ROWNUM = 1) AS lib_org,\n" +
                        "       lbv.prf_cod,lbv.dat_soin,lbv.cod_soc,lbv.num_fam,lbv.mat_pers,lbv.cod_visit,lbv.num_lig\n"
                        +
                        "FROM LIG_VISIT_ARRIVER lbv  WHERE lbv.cod_soc = :cod_soc AND lbv.mat_pers = :mat_pers AND lbv.num_fam = :num_fam"
                        +
                        " AND lbv.dat_soin = to_date(:dat_soin,'dd/mm/yyyy')", nativeQuery = true)
        List<LigVisitArriverProjection> findLigVisitArriverById(@Param("cod_soc") String codSoc,
                        @Param("mat_pers") String matPers,
                        @Param("num_fam") Integer numFam,
                        @Param("dat_soin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "delete from LIG_VISIT_ARRIVER where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and dat_act=:datAct and cod_visit=:codVisit and num_lig=:numLig", nativeQuery = true)
        void deleteLigVisitArriver(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin, @Param("abrv") String abrv, @Param("datAct") String datAct,
                        @Param("codVisit") String codVisit, @Param("numLig") String numLig);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM lig_visit_arriver WHERE cod_soc = :codSoc AND mat_pers = :matPers AND num_fam = :numFam AND dat_soin = to_date(:datSoin, 'dd/mm/yyyy')", nativeQuery = true)
        void deleteByBulletin(@Param("codSoc") String codSoc, @Param("matPers") String matPers,
                        @Param("numFam") Long numFam, @Param("datSoin") String datSoin);

        @Query("SELECT MAX(l.num_lig) FROM LigVisitArriver l WHERE l.cod_soc = :codSoc AND l.mat_pers = :matPers AND l.num_fam = :numFam AND l.dat_soin = :datSoin")
        Long findMaxNumLig(String codSoc, String matPers, Long numFam, java.time.LocalDate datSoin);

}
