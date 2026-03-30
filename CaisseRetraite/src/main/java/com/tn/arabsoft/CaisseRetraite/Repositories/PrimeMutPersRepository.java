package com.tn.arabsoft.CaisseRetraite.Repositories;

import com.tn.arabsoft.CaisseRetraite.Entities.Cles.ClePrimeMutPers;
import com.tn.arabsoft.CaisseRetraite.Entities.PrimeMutPers;
import com.tn.arabsoft.CaisseRetraite.Projections.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface PrimeMutPersRepository extends JpaRepository<PrimeMutPers, ClePrimeMutPers> {

  @Query(value = " select nvl(max(NUM_prime),0) + 1 \n" +
      "     from prime_mut_pers\n" +
      "     where cod_soc = :soc\n" +
      "     and mat_pers = :mat", nativeQuery = true)
  Long getMaxNumPrime(@Param("soc") String soc, @Param("mat") String mat);

  @Query(value = "select p.cod_soc,\n" +
      "       p.mat_pers,\n" +
      "       p.num_prime,\n" +
      "       p.typ_prime,\n" +
      "       p.dat_deb,\n" +
      "       p.dat_fin,\n" +
      "       p.mnt_a_payer,\n" +
      "       p.mnt_payer,\n" +
      "       p.mod_pay,\n" +
      "       p.ref_pay,\n" +
      "       p.dat_saisie,\n" +
      "       p.etat_prime,\n" +
      "       p.corps,\n" +
      "       p.cod_typ_depart,\n" +
      "       p.cod_affect,\n" +
      "       p.mnt_param,\n" +
      "       p.num_retr,\n" +
      "       p.seq_ecrt1,\n" +
      "       p.seq_ecrt2,\n" +
      "       p.imput,\n" +
      "       (select t.Nom_Pers || ' '||t.pren_pers from personnel t where p.mat_pers=t.mat_pers) nom,\n" +
      "       (select t.mat_int from personnel t where p.mat_pers=t.mat_pers) mat_int,\n" +
      "       (select t.dat_nais from personnel t where p.mat_pers=t.mat_pers) dat_nais\n" +
      "from prime_mut_pers p where  typ_prime='T' ", nativeQuery = true)
  List<PrimeMutPersProjection> getAll();

  @Query(value = "select p.cod_soc,\n" +
      "       p.mat_pers,\n" +
      "       p.num_prime,\n" +
      "       p.typ_prime,\n" +
      "       p.dat_deb,\n" +
      "       p.dat_fin,\n" +
      "       p.mnt_a_payer,\n" +
      "       p.mnt_payer,\n" +
      "       p.mod_pay,\n" +
      "       p.ref_pay,\n" +
      "       p.dat_saisie,\n" +
      "       p.etat_prime,\n" +
      "       p.corps,\n" +
      "       p.cod_typ_depart,\n" +
      "       p.cod_affect,\n" +
      "       p.mnt_param,\n" +
      "       p.num_retr,\n" +
      "       p.seq_ecrt1,\n" +
      "       p.seq_ecrt2,\n" +
      "       p.imput,(select t.Nom_Pers || ' '||t.pren_pers  from personnel t where p.mat_pers=t.mat_pers)nom from prime_mut_pers p where etat_prime='I'  ", nativeQuery = true)
  List<PrimeMutPersProjection> getAllVald();

  @Query(value = "select p.cod_soc,\n" +
      "       p.mat_pers,\n" +
      "       p.num_prime,\n" +
      "       p.typ_prime,\n" +
      "       p.dat_deb,\n" +
      "       p.dat_fin,\n" +
      "       p.mnt_a_payer,\n" +
      "       p.mnt_payer,\n" +
      "       p.mod_pay,\n" +
      "       p.ref_pay,\n" +
      "       p.dat_saisie,\n" +
      "       p.etat_prime,\n" +
      "       p.corps,\n" +
      "       p.cod_typ_depart,\n" +
      "       p.cod_affect,\n" +
      "       p.mnt_param,\n" +
      "       p.num_retr,\n" +
      "       p.seq_ecrt1,\n" +
      "       p.seq_ecrt2,\n" +
      "       p.imput,(select t.Nom_Pers || ' '||t.pren_pers  from personnel t where p.mat_pers=t.mat_pers)nom from prime_mut_pers p where p.mat_pers=:mat", nativeQuery = true)
  List<PrimeMutPersProjection> getAllByMat(@Param("mat") String mat);

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
      "pers_carte,(select lib_affect from affectation where cod_affect =p.cod_affect)libAffect," +
      "(select lib_typ_depart from type_depart where cod_typ_depart=p.cod_typ_depart)libDepart\n" +
      "from personnel p \n" +
      "where p.cod_soc = :codSoc \n" +

      "order by p.mat_pers\n", nativeQuery = true)
  List<PersCotisProjection> getPersPrime(@Param("codSoc") String codSoc);

  @Query(value = """
      SELECT p.cod_soc,
             p.mat_pers,
             p.num_prime,
             p.typ_prime,
             p.dat_deb,
             p.dat_fin,
             p.mnt_a_payer,
             p.mnt_payer,
             p.mod_pay,
             p.ref_pay,
             p.dat_saisie,
             p.etat_prime,
             p.corps,
             p.cod_typ_depart,
             p.cod_affect,
             p.mnt_param,
             p.num_retr,
             p.seq_ecrt1,
             p.seq_ecrt2,
             p.imput,
             (SELECT t.Nom_Pers || ' ' || t.pren_pers
              FROM personnel t
              WHERE p.mat_pers = t.mat_pers) AS nom
      FROM prime_mut_pers p
      WHERE p.cod_soc = :soc
        AND TO_CHAR(p.dat_deb, 'mm/yyyy') = :mois_
        AND p.mat_pers = nvl(:matricule, p.mat_pers)
        AND p.mat_pers IN (
            SELECT mat_pers
            FROM personnel
            WHERE corps = NVL(:corps, corps)
        )
        AND NVL(p.ETAT_PRIME, 'I') = 'I'
        """, nativeQuery = true)
  List<PrimeMutPersProjection> getPrimePersVal(
      @Param("soc") String soc,
      @Param("mois_") String mois_,
      @Param("matricule") String matricule, // can be null or empty string
      @Param("corps") String corps

  );

  @Query(value = """
    SELECT
        t.dat_disk,
        t.pret_disk,
        t.num_retr,
        t.mat_pers,
        t.montant,
        t.observation,
        t.valid,
        t.cod_grp_pret,
        t.typ_pret,
        t.cod_pret,
        (
            SELECT p.nom_pers || ' ' || p.pren_pers
            FROM personnel p
            WHERE p.mat_pers = t.mat_pers
        ) AS nom
    FROM disk_pret t
    WHERE t.valid IN ('I')
      AND (:mat IS NULL OR t.mat_pers = :mat)
      AND (
            :corps IS NULL
            OR t.mat_pers IN (
                SELECT p.mat_pers
                FROM personnel p
                WHERE p.corps = :corps
            )
          )
      AND TO_CHAR(t.dat_disk, 'MM/YYYY') = :mois
      AND t.pret_disk = '135'
    """, nativeQuery = true)
  List<DiskPretProjection> getDiskPret(
          @Param("mat") String mat,
          @Param("corps") String corps,
          @Param("mois") String mois
  );


  @Query(value = "SELECT P.MAT_PERS mat_pers, P.MAT_INT mat_int, P.DAT_NAIS dat_nais, P.NOM_PERS||' '||P.PREN_PERS nom, NUM_RETR num_retr\n"
      +
      "FROM PERSONNEL P \n" +
      "order by num_retr", nativeQuery = true)
  List<PersonnelPrimeProjection> getPersonnel();

  @Query(value = "SELECT  F.NUM_FAM, F.NOM_PREN, F.PARENTE\n" +
      "FROM FAMILLE F \n" +
      "where cod_soc=:soc\n" +
      "and mat_pers=:mat\n" +
      "union\n" +
      "select 0  num_fam,:nom,'A' from dual", nativeQuery = true)
  List<FamilleProjection> getFamille(@Param("soc") String soc, @Param("mat") String mat,
      @Param("nom") String nom);

  @Query(value = "select * from prime_mut_pers where cod_soc=:soc and mat_pers=:mat and num_prime=:num", nativeQuery = true)
  PrimeMutPers getById(@Param("soc") String soc, @Param("mat") String mat, @Param("num") Long num);


  @Query(value = "select p.cod_soc,\n" +
          "       p.mat_pers,\n" +
          "       p.num_prime,\n" +
          "       p.typ_prime,\n" +
          "       p.dat_deb,\n" +
          "       p.dat_fin,\n" +
          "       p.mnt_a_payer,\n" +
          "       p.mnt_payer,\n" +
          "       p.mod_pay,\n" +
          "       p.ref_pay,\n" +
          "       p.dat_saisie,\n" +
          "       p.etat_prime,\n" +
          "       p.corps,\n" +
          "       p.cod_typ_depart,\n" +
          "       p.cod_affect,\n" +
          "       p.mnt_param,\n" +
          "       p.num_retr,\n" +
          "       p.seq_ecrt1,\n" +
          "       p.seq_ecrt2,\n" +
          "       p.imput,\n" +
          "       (select t.Nom_Pers || ' '||t.pren_pers from personnel t where p.mat_pers=t.mat_pers) nom,\n" +
          "       (select t.mat_int from personnel t where p.mat_pers=t.mat_pers) mat_int,\n" +
          "       (select t.dat_nais from personnel t where p.mat_pers=t.mat_pers) dat_nais\n" +
          "from prime_mut_pers p where  typ_prime='T' and p.mat_pers=:mat", nativeQuery = true)
  List<PrimeMutPersProjection> getAllPrimeRet(@Param("mat") String mat);
}
