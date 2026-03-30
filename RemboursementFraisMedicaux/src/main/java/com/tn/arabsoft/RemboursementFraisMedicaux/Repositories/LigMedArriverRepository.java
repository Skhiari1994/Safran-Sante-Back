package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.LigMedArriverCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigAppArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigMedArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigMedArriverProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigVisitArriverProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LigMedArriverRepository extends JpaRepository<LigMedArriver, LigMedArriverCle> {
        @Query(value = "SELECT lbv.dat_act,\n" +
                        "                   lbv.cod_med,lbv.abrv_act,\n" +
                        "                   (SELECT a.lib_med \n" +
                        "                    FROM ref_med a \n" +
                        "                    WHERE a.cod_med = lbv.cod_med\n" +
                        "                    AND ROWNUM = 1) AS lib_med,  \n" +
                        "                   lbv.indice,\n" +
                        "                   lbv.mnt_honor,\n" +
                        "                   lbv.mnt_remb,\n" +
                        "                   lbv.mut_mnt_net,\n" +
                        "                   DECODE(lbv.prf_typ, \n" +
                        "                          1, 'Personne physique', \n" +
                        "                          2, 'Etablissement', \n" +
                        "                          'Autre') AS prf_type,   \n" +
                        "                   lbv.prf_typ,\n" +
                        "                   (SELECT re.etab_rsoc || ' ' || re.pr_rsoc \n" +
                        "                    FROM ref_etablis re \n" +
                        "                    WHERE re.prf_cod = lbv.prf_cod\n" +
                        "                    AND ROWNUM = 1) AS lib_org,\n" +
                        "                   lbv.prf_cod,lbv.dat_soin,lbv.cod_soc,lbv.num_fam,lbv.mat_pers,lbv.num_lig,\n"
                        +
                        "                    lbv.accord_med, lbv.nbr_j, lbv.num_pec_med\n" +
                        "            FROM lig_med_arriver lbv WHERE cod_soc = :cod_soc AND mat_pers = :mat_pers AND num_fam = :num_fam AND dat_soin = to_date(:dat_soin,'dd/mm/yyyy') ", nativeQuery = true)
        List<LigMedArriverProjection> findLigMedArriverById(@Param("cod_soc") String codSoc,
                        @Param("mat_pers") String matPers,
                        @Param("num_fam") Integer numFam,
                        @Param("dat_soin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "delete from lig_med_arriver where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and cod_med=:med "
                        +
                        "and num_lig=:numLig", nativeQuery = true)
        void deleteMedArriver(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin, @Param("abrv") String abrv, @Param("med") String med,
                        @Param("numLig") String numLig);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM lig_med_arriver WHERE cod_soc = :codSoc AND mat_pers = :matPers AND num_fam = :numFam AND dat_soin = to_date(:datSoin, 'dd/mm/yyyy')", nativeQuery = true)
        void deleteByBulletin(@Param("codSoc") String codSoc, @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam, @Param("datSoin") String datSoin);

        @Query("SELECT MAX(l.num_lig) FROM LigMedArriver  l WHERE l.cod_soc = :codSoc AND l.mat_pers = :matPers AND l.num_fam = :numFam AND l.dat_soin = :datSoin")
        Integer findMaxNumLig(@Param("codSoc") String codSoc, @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam, @Param("datSoin") LocalDate datSoin);

}
