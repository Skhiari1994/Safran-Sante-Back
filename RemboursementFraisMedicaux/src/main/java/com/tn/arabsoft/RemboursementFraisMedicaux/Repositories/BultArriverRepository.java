package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BultArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BultArriverCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BultArriverLibreCnamProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.BultArriverProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LovNumFilCnamLibre;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BultArriverRepository extends JpaRepository<BultArriver, BultArriverCle> {

        @Query(value = "SELECT * FROM bult_arriver  WHERE cod_soc = :cod_soc AND mat_pers = :mat_pers AND num_fam = :num_fam AND dat_soin = to_date(:dat_soin,'dd/mm/yyyy')", nativeQuery = true)
        BultArriver findBultArriverById(@Param("cod_soc") String codSoc,
                        @Param("mat_pers") String matPers,
                        @Param("num_fam") Integer numFam,
                        @Param("dat_soin") String datSoin);

        @Query(value = "SELECT " +
                        "    ba.cod_soc, p.num_retr, " +
                        "    ba.mat_pers, ba.tot_net," +
                        "    (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom, " +
                        "    ba.cod_fil, ba.cod_bord, ba.cod_assur, " +
                        "    ba.num_fam, " +
                        "    NVL(f.nom_pren,'Adhérent') AS nom, " +
                        "    CASE " +
                        "        WHEN ba.num_fam = '0' THEN 'Adhérent' " +
                        "        WHEN ba.num_fam = '99' THEN 'Conjoint' " +
                        "        WHEN ba.num_fam BETWEEN 1 AND 10 THEN 'Enfant' " +
                        "        ELSE 'autre' " +
                        "    END AS statut_famille, " +
                        "    ba.dat_soin, " +
                        "    (SELECT lib_remb FROM regime_remb rr WHERE rr.reg_remb = ba.reg_remb) AS lib_remb, " +
                        "    ba.reg_remb AS reg_remb, " +
                        "    ba.tot_remb " +
                        "FROM bult_soin ba " +
                        "JOIN personnel p ON ba.mat_pers = p.mat_pers " +
                        "LEFT JOIN famille f ON ba.num_fam = f.num_fam AND p.mat_pers = f.mat_pers " +
                        "WHERE ba.cod_bord = :cod_bord " +
                        "ORDER BY ba.dat_soin DESC", nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBord(@Param("cod_bord") String cod_bord);

        @Query(value = "SELECT " +
                        "    ba.cod_soc, p.num_retr, " +
                        "    ba.mat_pers, ba.tot_net," +
                        "    (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom, " +
                        "    ba.cod_fil, ba.cod_bord, ba.cod_assur, " +
                        "    ba.num_fam, " +
                        "    NVL(f.nom_pren,'Adhérent') AS nom, " +
                        "    CASE " +
                        "        WHEN ba.num_fam = '0' THEN 'Adhérent' " +
                        "        WHEN ba.num_fam = '99' THEN 'Conjoint' " +
                        "        WHEN ba.num_fam BETWEEN 1 AND 10 THEN 'Enfant' " +
                        "        ELSE 'autre' " +
                        "    END AS statut_famille, " +
                        "    ba.dat_soin, " +
                        "    (SELECT lib_remb FROM regime_remb rr WHERE rr.reg_remb = ba.reg_remb) AS lib_remb, " +
                        "    ba.reg_remb AS reg_remb, " +
                        "    ba.tot_remb " +
                        "FROM bult_arriver ba " +
                        "JOIN personnel p ON ba.mat_pers = p.mat_pers " +
                        "LEFT JOIN famille f ON ba.num_fam = f.num_fam AND p.mat_pers = f.mat_pers " +
                        "WHERE ba.cod_bord = :cod_bord " +
                        "ORDER BY ba.dat_soin DESC", nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBord2(@Param("cod_bord") String cod_bord);

        @Query(value = "SELECT " +
                        "    ba.cod_soc, " +
                        "    p.num_retr, " +
                        "    ba.mat_pers, " +
                        "    ba.tot_net, " +
                        "    (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom, " +
                        "    ba.cod_fil, " +
                        "    ba.cod_bord, " +
                        "    ba.cod_assur, " +
                        "    ba.num_fam, " +
                        "    NVL(f.nom_pren,'Adhérent') AS nom, " +
                        "    CASE " +
                        "        WHEN ba.num_fam = '0' THEN 'Adhérent' " +
                        "        WHEN ba.num_fam = '99' THEN 'Conjoint' " +
                        "        WHEN ba.num_fam BETWEEN 1 AND 10 THEN 'Enfant' " +
                        "        ELSE 'autre' " +
                        "    END AS statut_famille, " +
                        "    ba.dat_soin, " +
                        "    ba.dat_saisie, " +
                        "    (SELECT lib_remb FROM regime_remb rr WHERE rr.reg_remb = ba.reg_remb) AS lib_remb, " +
                        "    ba.tot_remb " +
                        "FROM bult_soin ba " +
                        "JOIN personnel p ON ba.mat_pers = p.mat_pers " +
                        "LEFT JOIN famille f ON ba.num_fam = f.num_fam AND p.mat_pers = f.mat_pers " +
                        "WHERE ba.cod_bord = :cod_bord " +
                        "ORDER BY ba.dat_saisie DESC, ba.mat_pers, ba.num_fam", nativeQuery = true)
        List<BultArriverProjection> findBultEnvoiByCodBord(@Param("cod_bord") String cod_bord);

        @Query(value = "SELECT " +
                        "    ba.cod_soc, p.num_retr, " +
                        "    ba.mat_pers, ba.tot_net," +
                        "    (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom, " +
                        "    ba.cod_fil, ba.cod_bord, ba.cod_assur, " +
                        "    ba.num_fam, " +
                        "    NVL(f.nom_pren,'Adhérent') AS nom, " +
                        "    CASE " +
                        "        WHEN ba.num_fam = '0' THEN 'Adhérent' " +
                        "        WHEN ba.num_fam = '99' THEN 'Conjoint' " +
                        "        WHEN ba.num_fam BETWEEN 1 AND 10 THEN 'Enfant' " +
                        "        ELSE 'autre' " +
                        "    END AS statut_famille, " +
                        "    ba.dat_soin, " +
                        "    (SELECT lib_remb FROM regime_remb rr WHERE rr.reg_remb = ba.reg_remb) AS lib_remb, " +
                        "    ba.tot_remb, " +
                        "    (SELECT NVL(SUM(NVL(mnt_remb,0)),0) FROM lig_bult_arriver " +
                        "     WHERE cod_soc = ba.cod_soc AND mat_pers = ba.mat_pers " +
                        "     AND num_fam = ba.num_fam AND dat_soin = ba.dat_soin) tot_mut, " +
                        "    (SELECT NVL(PLAFOND,0) - NVL(SOLD_PLAF,0) FROM plafond_cnam " +
                        "     WHERE cod_soc = ba.cod_soc AND MAT_PERS = ba.MAT_PERS " +
                        "     AND ANNEE_CNAM = TO_NUMBER(TO_CHAR(ba.dat_soin,'yyyy'))) solde " +
                        "FROM bult_arriver ba " +
                        "JOIN personnel p ON ba.mat_pers = p.mat_pers " +
                        "LEFT JOIN famille f ON ba.num_fam = f.num_fam AND p.mat_pers = f.mat_pers " +
                        "WHERE ba.cod_bord = :cod_bord " +
                        "ORDER BY ba.dat_soin DESC", nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBord4(@Param("cod_bord") String cod_bord);

        @Query(value = "SELECT \n" +
                        "    ba.cod_soc, p.num_retr,\n" +
                        "    ba.mat_pers, \n" +
                        "    (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom,\n" +
                        "    ba.cod_fil,ba.cod_bord,ba.cod_assur,\n" +
                        "    ba.num_fam,\n" +
                        "    nvl(f.nom_pren,'Adhérent') as nom,\n" +
                        "    CASE\n" +
                        "        WHEN ba.num_fam = '0' THEN 'Adhérent'\n" +
                        "        WHEN ba.num_fam = '99' THEN 'Conjoint'\n" +
                        "        WHEN ba.num_fam BETWEEN 1 AND 10 THEN 'Enfant'\n" +
                        "        ELSE 'autre'\n" +
                        "    END AS statut_famille,\n" +
                        "    ba.dat_soin, \n" +
                        "    (SELECT lib_remb \n" +
                        "     FROM regime_remb rr \n" +
                        "     WHERE rr.reg_remb = ba.reg_remb) AS lib_remb,\n" +
                        "    ba.tot_remb\n" +
                        "FROM \n" +
                        "    bult_soin ba\n" +
                        "JOIN personnel p ON ba.mat_pers = p.mat_pers\n" +
                        "LEFT JOIN famille f ON ba.num_fam = f.num_fam AND p.mat_pers = f.mat_pers\n" +
                        "WHERE \n" +
                        "    ba.cod_bord = :cod_bord\n" +
                        "ORDER BY ba.dat_soin DESC", nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBordRecep(@Param("cod_bord") String cod_bord);

        @Query(value = "SELECT\n" +
                        "    ba.dat_saisie              AS dat_saisie,\n" +
                        "    ba.cod_soc                 AS cod_soc,\n" +
                        "    ba.mat_pers                AS mat_pers,\n" +
                        "    (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom,\n" +
                        "    ba.cod_fil                 AS cod_fil,\n" +
                        "    ba.num_fam                 AS num_fam,\n" +
                        "    ba.tot_net                 AS tot_net,\n" +
                        "    NVL(f.nom_pren,'Adhérent') AS nom,\n" +
                        "\n" +
                        "    CASE\n" +
                        "        WHEN ba.num_fam = '0' THEN 'Adhérent'\n" +
                        "        WHEN ba.num_fam = '99' THEN 'Conjoint'\n" +
                        "        WHEN ba.num_fam BETWEEN 1 AND 10 THEN 'Enfant'\n" +
                        "        ELSE 'Autre'\n" +
                        "    END AS statut_famille,\n" +
                        "\n" +
                        "    ba.dat_soin                AS dat_soin,\n" +
                        "\n" +
                        "    (SELECT lib_remb\n" +
                        "       FROM regime_remb rr\n" +
                        "      WHERE rr.reg_remb = ba.reg_remb) AS lib_remb,\n" +
                        "\n" +
                        "    ba.tot_remb                AS tot_remb,\n" +
                        "\n" +
                        "    (SELECT NVL(SUM(NVL(mnt_remb,0)),0)\n" +
                        "       FROM lig_bult_arriver\n" +
                        "      WHERE cod_soc = ba.cod_soc\n" +
                        "        AND mat_pers = ba.mat_pers\n" +
                        "        AND num_fam = ba.num_fam\n" +
                        "        AND dat_soin = ba.dat_soin) AS tot_mut,\n" +
                        "\n" +
                        "    DECODE(ba.reg_adh,'O','Oui','N','Non','') AS reg_adh,\n" +
                        "\n" +
                        "    (SELECT NVL(PLAFOND,0) - NVL(SOLD_PLAF,0)\n" +
                        "       FROM plafond_cnam\n" +
                        "      WHERE cod_soc = ba.cod_soc\n" +
                        "        AND mat_pers = ba.mat_pers\n" +
                        "        AND annee_cnam = TO_NUMBER(TO_CHAR(ba.dat_soin,'YYYY'))) AS solde,\n" +
                        "\n" +
                        "    ba.reclam                  AS reclamation,\n" +
                        "    ba.mod_pay                 AS mod_pay,\n" +
                        "    ba.dat_vir                 AS dat_vir,\n" +
                        "    p.num_retr                AS num_retr,\n" +
                        "    ba.cod_assur               AS cod_assur,\n" +
                        "    ba.cod_bord                AS cod_bord,\n" +
                        "    ba.reg_remb                AS reg_remb\n" +
                        "\n" +
                        "FROM bult_arriver ba\n" +
                        "JOIN personnel p\n" +
                        "  ON ba.mat_pers = p.mat_pers\n" +
                        "LEFT JOIN famille f\n" +
                        "  ON ba.num_fam = f.num_fam\n" +
                        " AND p.mat_pers = f.mat_pers\n" +
                        "\n" +
                        "WHERE ba.cod_bord = :cod_bord\n" +
                        "ORDER BY ba.dat_saisie DESC, ba.mat_pers, ba.num_fam\n", nativeQuery = true)
        List<BultArriverProjection> findBultArriverReg(@Param("cod_bord") String cod_bord);

        @Query(value = "select ann_plaf_imp,\n" +
                        "       t.cod_assur,\n" +
                        "       cod_bord,\n" +
                        "       cod_fil,\n" +
                        "       cod_malad,\n" +
                        "       t.cod_soc,\n" +
                        "       dat_prev_accouch,\n" +
                        "       dat_saisie,\n" +
                        "       dat_soin,\n" +
                        "       dat_vir,\n" +
                        "       decis_med,\n" +
                        "       envoi,\n" +
                        "       t.mat_pers,\n" +
                        "       (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom,\n" +
                        "p.num_retr ,\n" +
                        "  CASE\n" +
                        "        WHEN f.nom_pren IS NOT NULL AND f.nom_pren IS NOT NULL THEN f.nom_pren\n" +
                        "        ELSE\n" +
                        "            CASE\n" +
                        "                WHEN t.num_fam = '0' THEN 'Adhérent'\n" +
                        "                WHEN t.num_fam = '99' THEN 'Conjoint'\n" +
                        "                WHEN t.num_fam BETWEEN '1' AND '10' THEN 'Enfant'\n" +
                        "                ELSE 'Autre'\n" +
                        "            END\n" +
                        "    END AS nom_pren,\n" +
                        "       t.mat_pers_conj,\n" +
                        "       mod_pay,\n" +
                        "       nat_bult,\n" +
                        "       t.num_assur,\n" +
                        "       t.num_ass_conj,\n" +
                        "       t.num_fam,\n" +
                        "       num_pec,\n" +
                        "       t.num_soin,\n" +
                        "       num_soin_cnam,\n" +
                        "       obs,\n" +
                        "       obs_a,\n" +
                        "       ord_bult,\n" +
                        "       reclam,\n" +
                        "       reg_adh,\n" +
                        "       reg_remb,\n" +
                        "       tot_honor,\n" +
                        "       tot_net,\n" +
                        "       tot_remb,\n" +
                        "       tot_remb_med,\n" +
                        "       typ_bult\n" +
                        "  from bult_arriver t\n" +
                        "  JOIN personnel p\n" +
                        "    ON t.mat_pers = p.mat_pers\n" +
                        "LEFT JOIN famille f ON t.num_fam = f.num_fam AND p.mat_pers = f.mat_pers\n" +
                        " where t.cod_bord = :cod_bord_\n" +
                        " order by dat_saisie DESC, mat_pers, num_fam\n", nativeQuery = true)
        List<BultArriverLibreCnamProjection> findBultArriverByCodBordLibreCnam(@Param("cod_bord") String cod_bord_);

        @Query(value = "SELECT p.mat_pers, p.nom_pers || ' ' || p.pren_pers AS nom_prenom, a.cod_fil, p.num_retr " +
                        "FROM personnel p, pers_affil a " +
                        "WHERE p.cod_soc = :codSoc " +
                        "AND p.cod_soc = a.cod_soc " +
                        "AND p.mat_pers = a.mat_pers " +
                        "AND a.courant = 'O'", countQuery = "SELECT count(*) FROM personnel p, pers_affil a " +
                                        "WHERE p.cod_soc = :codSoc " +
                                        "AND p.cod_soc = a.cod_soc " +
                                        "AND p.mat_pers = a.mat_pers " +
                                        "AND a.courant = 'O'", nativeQuery = true)
        Page<LovNumFilCnamLibre> lovNumFilCnamLibre(@Param("codSoc") String codSoc, Pageable pageable);

        @Query(value = "SELECT \n" +
                        "    ba.cod_soc, \n" +
                        "    ba.mat_pers, \n" +
                        "    (p.nom_pers || ' ' || p.pren_pers) AS nom_prenom,\n" +
                        "    ba.cod_fil,\n" +
                        "    ba.num_fam,\n" +
                        "    nvl(f.nom_pren,'Adhérent') as nom,\n" +
                        "    CASE  WHEN ba.num_fam = '0' THEN 'Adhérent'\n" +
                        "        WHEN ba.num_fam = '99' THEN 'Conjoint'\n" +
                        "        WHEN ba.num_fam BETWEEN 1 AND 10 THEN 'Enfant'\n" +
                        "        ELSE 'autre'\n" +
                        "    END AS statut_famille,\n" +
                        "    ba.dat_soin, \n" +
                        "    (SELECT lib_remb \n" +
                        "     FROM regime_remb rr \n" +
                        "     WHERE rr.reg_remb = ba.reg_remb) AS lib_remb,\n" +
                        "    ba.tot_remb, \n" +
                        " (select nvl(sum(nvl(mnt_remb,0)),0)   from lig_bult_arriver\n" +
                        "    where cod_soc =ba.cod_soc\n" +
                        "    and mat_pers = ba.mat_pers\n" +
                        "  and num_fam = ba.num_fam\n" +
                        "and dat_soin = ba.dat_soin) tot_mut,decode(reg_adh,'O','Oui','N','Non','')reg_adh, \n" +
                        "(select nvl(PLAFOND,0) - nvl(SOLD_PLAF,0) \n" +
                        "from plafond_cnam\n" +
                        "where cod_soc = ba.cod_soc\n" +
                        "and MAT_PERS = ba.MAT_PERS\n" +
                        "and ANNEE_CNAM = to_number(to_char(ba.dat_soin,'yyyy')))solde,ba.reclam reclamation,ba.mod_pay,ba.dat_vir \n"
                        +
                        "FROM  bult_arriver ba\n" +
                        "JOIN personnel p ON ba.mat_pers = p.mat_pers\n" +
                        "LEFT JOIN famille f ON ba.num_fam = f.num_fam AND p.mat_pers = f.mat_pers\n" +
                        "WHERE  ba.cod_bord = :cod_bord and  mod_pay='V'  and nvl(reg_adh,'N')='N' order by dat_saisie DESC, mat_pers, num_fam\n", nativeQuery = true)
        List<BultArriverProjection> findBultArriverCptCnam(@Param("cod_bord") String cod_bord);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM bult_arriver WHERE cod_soc = :soc AND mat_pers = :mat AND num_fam = :fam AND dat_soin = to_date(:datSoin, 'dd/mm/yyyy')", nativeQuery = true)
        void deleteBulletin(@Param("soc") String soc, @Param("mat") String mat, @Param("fam") String fam,
                        @Param("datSoin") String datSoin);

}
