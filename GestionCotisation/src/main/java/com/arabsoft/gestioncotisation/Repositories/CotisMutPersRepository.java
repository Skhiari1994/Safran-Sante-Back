package com.arabsoft.gestioncotisation.Repositories;

import com.arabsoft.gestioncotisation.Entities.Cle.CotisMutPersID;
import com.arabsoft.gestioncotisation.Entities.CotisMutPers;
import com.arabsoft.gestioncotisation.Entities.LigCotisMutPers;
import com.arabsoft.gestioncotisation.Projections.CotisMutPersProjection;
import com.arabsoft.gestioncotisation.Projections.PersCotisProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CotisMutPersRepository extends JpaRepository<CotisMutPers, CotisMutPersID> {
    Page<CotisMutPers> findAll(Pageable pageable);

    @Query("SELECT c FROM CotisMutPers c WHERE c.cod_soc = :codSoc AND c.mat_pers = :matPers AND c.num_cot = :numCot")
    Optional<CotisMutPers> findByCodSocAndMatPersAndNumCot(
            @Param("codSoc") String codSoc,
            @Param("matPers") String matPers,
            @Param("numCot") Long numCot);

    @Query("SELECT c FROM CotisMutPers c WHERE c.cod_soc = :codSoc AND c.mat_pers = :matPers ")
    List<CotisMutPers> findByCodSocAndMatPers(
            @Param("codSoc") String codSoc,
            @Param("matPers") String matPers);
    @Query(value = "select cod_soc, \n" +
            "mat_pers, \n" +
            "cod_assur, \n" +
            "nom_pers, \n" +
            "sexe, \n" +
            "cin, \n" +
            "dat_nais, \n" +
            "pren_pers, \n" +
            "dat_emb, \n" +
            "cod_sit, \n" +
            "nbr_enf, \n" +
            "cod_retr, \n" +
            "num_retr, \n" +
            "num_assur, \n" +
            "dat_ass, \n" +
            "cod_pay, \n" +
            "rib, \n" +
            "nom_pers_a, \n" +
            "pren_pers_a, \n" +
            "cod_natp, \n" +
            "cod_banq, \n" +
            "cod_agc, \n" +
            "dat_dece, \n" +
            "etat_act, \n" +
            "dat_motif, \n" +
            "cod_lieu_geog, \n" +
            "bas_plafond, \n" +
            "nom_jf, \n" +
            "nom_jf_a, \n" +
            "photo_pers, \n" +
            "lieu_nais, \n" +
            "mnt_param, \n" +
            "etat_prof, \n" +
            "dat_aff_cnam, \n" +
            "corps, \n" +
            "cod_affect, \n" +
            "dat_affect, \n" +
            "cod_typ_depart, \n" +
            "dat_depart, \n" +
            "typ_aff, \n" +
            "cod_user, \n" +
            "dat_maj, \n" +
            "mat_int, \n" +
            "pers_carte\n" +
            "from personnel p \n" +
            "where p.cod_soc = :codSoc \n" +
            "  and p.cod_typ_depart = '01'\n" +
            "  and etat_act = 'A'\n" +
            "order by p.mat_pers\n",nativeQuery = true)
    List<PersCotisProjection> getPersCotis(@Param("codSoc") String codSoc);
    @Query(value = "select cod_soc, \n" +
            "mat_pers, \n" +
            "cod_assur, \n" +
            "nom_pers, \n" +
            "sexe, \n" +
            "cin, \n" +
            "dat_nais, \n" +
            "pren_pers, \n" +
            "dat_emb, \n" +
            "cod_sit, \n" +
            "nbr_enf, \n" +
            "cod_retr, \n" +
            "num_retr, \n" +
            "num_assur, \n" +
            "dat_ass, \n" +
            "cod_pay, \n" +
            "rib, \n" +
            "nom_pers_a, \n" +
            "pren_pers_a, \n" +
            "cod_natp, \n" +
            "cod_banq, \n" +
            "cod_agc, \n" +
            "dat_dece, \n" +
            "etat_act, \n" +
            "dat_motif, \n" +
            "cod_lieu_geog, \n" +
            "bas_plafond, \n" +
            "nom_jf, \n" +
            "nom_jf_a, \n" +
            "photo_pers, \n" +
            "lieu_nais, \n" +
            "mnt_param, \n" +
            "etat_prof, \n" +
            "dat_aff_cnam, \n" +
            "corps, \n" +
            "cod_affect, \n" +
            "dat_affect, \n" +
            "cod_typ_depart, \n" +
            "dat_depart, \n" +
            "typ_aff, \n" +
            "cod_user, \n" +
            "dat_maj, \n" +
            "mat_int, \n" +
            "pers_carte\n" +
            "from personnel p \n" +
            "where p.cod_soc = :codSoc    and p.cod_typ_depart = '01'\n" +
            "  and etat_act = 'A'\n" +
            "order by p.mat_pers\n",nativeQuery = true)
    List<PersCotisProjection> getPersCotisSearch(@Param("codSoc") String codSoc);
    @Query("SELECT COALESCE(MAX(c.num_cot), 0) + 1 FROM CotisMutPers c WHERE c.cod_soc = :codSoc AND c.mat_pers = :matPers")
    Long findNextNumCot(@Param("codSoc") String codSoc, @Param("matPers") String matPers);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM CotisMutPers c WHERE c.cod_soc = :codSoc AND c.mat_pers = :matPers AND c.dat_deb = :datDeb")
    boolean existsByCodSocAndMatPersAndDatDeb(@Param("codSoc") String codSoc, @Param("matPers") String matPers, @Param("datDeb") LocalDate datDeb);
 //   boolean existsByCod_socAndMat_persAndDat_deb(String cod_soc, String mat_pers, LocalDate dat_deb);

    @Query("SELECT c.dat_deb FROM CotisMutPers c WHERE c.cod_soc = :codSoc AND c.mat_pers = :matPers AND c.dat_deb BETWEEN :startDate AND :endDate")
    List<LigCotisMutPers> findExistingDatesInRange(
            @Param("codSoc") String codSoc,
            @Param("matPers") String matPers,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query(value = "select cod_soc, \n" +
            "mat_pers, \n" +
            "cod_assur, \n" +
            "nom_pers, \n" +
            "sexe, \n" +
            "cin, \n" +
            "dat_nais, \n" +
            "pren_pers, \n" +
            "dat_emb, \n" +
            "cod_sit, \n" +
            "nbr_enf, \n" +
            "cod_retr, \n" +
            "num_retr, \n" +
            "num_assur, \n" +
            "dat_ass, \n" +
            "cod_pay, \n" +
            "rib, \n" +
            "nom_pers_a, \n" +
            "pren_pers_a, \n" +
            "cod_natp, \n" +
            "cod_banq, \n" +
            "cod_agc, \n" +
            "dat_dece, \n" +
            "etat_act, \n" +
            "dat_motif, \n" +
            "cod_lieu_geog, \n" +
            "bas_plafond, \n" +
            "nom_jf, \n" +
            "nom_jf_a, \n" +
            "photo_pers, \n" +
            "lieu_nais, \n" +
            "mnt_param, \n" +
            "etat_prof, \n" +
            "dat_aff_cnam, \n" +
            "corps, \n" +
            "cod_affect, \n" +
            "dat_affect, \n" +
            "cod_typ_depart, \n" +
            "dat_depart, \n" +
            "typ_aff, \n" +
            "cod_user, \n" +
            "dat_maj, \n" +
            "mat_int, \n" +
            "pers_carte\n" +
            "from personnel p \n" +
            "where p.cod_soc = :codSoc \n",nativeQuery = true)
    List<PersCotisProjection> getPersCotisActif(@Param("codSoc") String codSoc);

    @Query(value = " SELECT \n" +
            "  c.cod_soc,\n" +
            "  c.mat_pers,\n" +
            "  (SELECT nom_pers || ' ' || pren_pers \n" +
            "     FROM personnel \n" +
            "    WHERE mat_pers = c.mat_pers) AS nom_pers,\n" +
            "  num_cot,\n" +
            "  typ_cot,\n" +
            "  dat_deb,\n" +
            "  dat_fin,\n" +
            "  mnt_a_payer,\n" +
            "  mnt_payer,\n" +
            "  mod_pay,\n" +
            "  ref_pay,\n" +
            "  dat_saisie,\n" +
            "  etat_cot,\n" +
            "  corps,\n" +
            "  cod_typ_depart,\n" +
            "  cod_affect,\n" +
            "  mnt_param,\n" +
            "  num_retr,\n" +
            "  seq_ecrt1,\n" +
            "  seq_ecrt2,\n" +
            "  imput\n" +
            "FROM COTIS_MUT_PERS c\n" +
            "WHERE cod_soc = :codSoc\n" +
            "  AND TO_CHAR(dat_deb, 'dd/mm/yyyy') = :mois \n" +
            "  AND mat_pers IN (\n" +
            "        SELECT mat_pers \n" +
            "          FROM personnel \n" +
            "         WHERE corps = NVL(:corps, corps)\n" +
            "      )\n" +
            "  AND NVL(ETAT_COT, 'I') = 'I'\n" +
            "ORDER BY LPAD(TO_CHAR(num_retr), 10, '0') \n" ,nativeQuery = true)
    List<CotisMutPersProjection> getPersCotisActifCorps(@Param("codSoc") String codSoc, @Param("corps") String corps, @Param("mois") LocalDate mois);


    @Query(value = "select  cod_soc, \n" +
            "mat_pers, \n" +
            "num_cot, \n" +
            "typ_cot, \n" +
            "dat_deb, \n" +
            "dat_fin, \n" +
            "mnt_a_payer, \n" +
            "mnt_payer, \n" +
            "mod_pay, \n" +
            "ref_pay, \n" +
            "dat_saisie, \n" +
            "etat_cot, \n" +
            "corps, \n" +
            "cod_typ_depart, \n" +
            "cod_affect, \n" +
            "mnt_param, \n" +
            "num_retr, \n" +
            "seq_ecrt1, \n" +
            "seq_ecrt2, \n" +
            "imput\n" +
            "\n" +
            "from cotis_mut_pers c\n" +
            "where c.cod_soc = :codSoc\n" +
            "  and c.etat_cot='I'\n" +
            "  and c.mat_pers= :matPers  and dat_deb=nvl(:startDate,dat_deb) and dat_fin=nvl(:endDate,dat_fin) \n" +
            "  order by c.mat_pers ",nativeQuery = true)
    List<CotisMutPers> getCotisationMatDate(
            @Param("codSoc") String codSoc,
            @Param("matPers") String matPers,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
    @Query(value = "select  cod_soc, \n" +
            "mat_pers, \n" +
            "num_cot, \n" +
            "typ_cot, \n" +
            "dat_deb, \n" +
            "dat_fin, \n" +
            "mnt_a_payer, \n" +
            "mnt_payer, \n" +
            "mod_pay, \n" +
            "ref_pay, \n" +
            "dat_saisie, \n" +
            "etat_cot, \n" +
            "corps, \n" +
            "cod_typ_depart, \n" +
            "cod_affect, \n" +
            "mnt_param, \n" +
            "num_retr, \n" +
            "seq_ecrt1, \n" +
            "seq_ecrt2, \n" +
            "imput\n" +
            "\n" +
            "from cotis_mut_pers c\n" +
            "where c.cod_soc = :codSoc\n" +

            "  and c.mat_pers= :matPers \n" +
            "  order by c.mat_pers , c.num_cot desc",nativeQuery = true)
    List<CotisMutPers> getCotisationMat(
            @Param("codSoc") String codSoc,
            @Param("matPers") String matPers
    );
    @Query(value = "dat_disk, \n" +
            "pret_disk, \n" +
            "num_retr, \n" +
            "d.mat_pers, \n" +
            "(select nom_pers||' '||pren_pers from personnel\n" +
            "  where mat_pers=d.mat_pers)\n" +
            "montant, \n" +
            "observation, \n" +
            "valid, \n" +
            "cod_grp_pret, \n" +
            "typ_pret, \n" +
            "cod_pret\n" +
            "\n" +
            "  FROM disk_pret d\n" +
            " WHERE VALID IN ('I', 'V')\n" +
            "   AND mat_pers = NVL(NULL, mat_pers) \n" +
            "   AND mat_pers IN (\n" +
            "         SELECT mat_pers \n" +
            "           FROM personnel \n" +
            "          WHERE corps = NVL(:corps, corps)\n" +
            "       )\n" +
            "   AND TO_CHAR(dat_disk, 'mm/yyyy') = :mois\n" +
            "   AND pret_disk = '565'\n" +
            " ORDER BY LPAD(TO_CHAR(num_retr), 10, '0')" ,nativeQuery = true)
    List<CotisMutPersProjection> getPersAutoCotisAgtAct( @Param("corps") String corps, @Param("mois") LocalDate mois);

}
