package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BultArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.LigActArriverCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigActArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigActArriverProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LigActArriverRepository extends JpaRepository<LigActArriver, LigActArriverCle> {

        @Query(value = "SELECT lbv.dat_act,\n" +
                        "       lbv.cod_act,\n" +
                        "       (SELECT a.lib_act \n" +
                        "        FROM ref_act a \n" +
                        "        WHERE a.cod_act = lbv.cod_act\n" +
                        "        AND ROWNUM = 1) AS lib_act,  \n" +
                        "       lbv.indice,\n" +
                        "       lbv.mnt_honor,\n" +
                        "       lbv.mnt_remb,\n" +
                        "       lbv.mut_mnt_net,\n" +
                        "       lbv.accord_act,\n" +
                        "       lbv.num_pec_act,\n" +
                        "       DECODE(lbv.prf_typ, \n" +
                        "              1, 'Personne physique', \n" +
                        "              2, 'Etablissement', \n" +
                        "              'Autre') AS prf_type,\n" +
                        "       lbv.prf_typ AS type_etablis,\n" +
                        "       (SELECT re.etab_rsoc || ' ' || re.pr_rsoc \n" +
                        "        FROM ref_etablis re \n" +
                        "        WHERE re.prf_typ = lbv.prf_typ\n" +
                        "        AND re.prf_cod = lbv.prf_cod) AS lib_etablis,\n" + // Changed: matches prf_typ too
                        "       (SELECT re.etab_rsoc || ' ' || re.pr_rsoc \n" +
                        "        FROM ref_etablis re \n" +
                        "        WHERE re.prf_cod = lbv.prf_cod\n" +
                        "        AND ROWNUM = 1) AS lib_org,\n" +
                        "       lbv.dat_soin,\n" +
                        "       lbv.cod_soc,\n" +
                        "       lbv.num_fam,\n" +
                        "       lbv.mat_pers,\n" +
                        "       lbv.dat_act,\n" +
                        "       lbv.num_lig,\n" +
                        "       lbv.abrv_act,\n" +
                        "       lbv.prf_cod\n" +
                        "FROM LIG_ACT_ARRIVER lbv WHERE cod_soc = :cod_soc AND mat_pers = :mat_pers AND num_fam = :num_fam AND dat_soin = to_date(:dat_soin,'dd/mm/yyyy')", nativeQuery = true)
        List<LigActArriverProjection> findLigActArriverById(@Param("cod_soc") String codSoc,
                        @Param("mat_pers") String matPers,
                        @Param("num_fam") Integer numFam,
                        @Param("dat_soin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "delete from LIG_ACT_ARRIVER where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and num_lig=:numLig and dat_act=:datAct and cod_act=:codAct", nativeQuery = true)
        void deleteLigActArriver(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin, @Param("abrv") String abrv, @Param("numLig") String numLig,
                        @Param("datAct") String datAct, @Param("codAct") String codAct);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM lig_act_arriver WHERE cod_soc = :codSoc AND mat_pers = :matPers AND num_fam = :numFam AND dat_soin = to_date(:datSoin, 'dd/mm/yyyy')", nativeQuery = true)
        void deleteByBulletin(@Param("codSoc") String codSoc, @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam, @Param("datSoin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "INSERT INTO lig_act_arriver (cod_soc, mat_pers, num_fam, dat_soin, indice, abrv_act, cod_act, num_lig, let_cod, cot_act, act_prix, mnt_honor, mnt_remb, accord_act, mnt_net, dat_act, prf_typ, prf_cod, mut_mnt_net, num_pec_act, decis_act) "
                        +
                        "VALUES (:#{#acte.cod_soc}, :#{#acte.mat_pers}, :#{#acte.num_fam}, :#{#acte.dat_soin}, :#{#acte.indice}, :#{#acte.abrv_act}, :#{#acte.cod_act}, :#{#acte.num_lig}, :#{#acte.let_cod}, :#{#acte.cot_act}, :#{#acte.act_prix}, :#{#acte.mnt_honor}, :#{#acte.mnt_remb}, :#{#acte.accord_act}, :#{#acte.mnt_net}, :#{#acte.dat_act}, :#{#acte.prf_typ}, :#{#acte.prf_cod}, :#{#acte.mut_mnt_net}, :#{#acte.num_pec_act}, :#{#acte.decis_act})", nativeQuery = true)
        void insertActe(@Param("acte") LigActArriver acte);

        @Query("SELECT MAX(l.num_lig) FROM LigActArriver l WHERE l.cod_soc = :codSoc AND l.mat_pers = :matPers AND l.num_fam = :numFam AND l.dat_soin = :datSoin")
        Integer findMaxNumLig(String codSoc, String matPers, Integer numFam, java.time.LocalDate datSoin);
}
