package com.arabsoft.gestionindemnites.Repositories;

import com.arabsoft.gestionindemnites.Entities.Cle.CleDemandeDons;
import com.arabsoft.gestionindemnites.Entities.DemandeDons;

import com.arabsoft.gestionindemnites.Projections.DemDonsProjection;
import com.arabsoft.gestionindemnites.Projections.DemandeDonProjection;
import com.arabsoft.gestionindemnites.Projections.DemandeDonsPers;
import com.arabsoft.gestionindemnites.Projections.FamilleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandeDonsRepository extends JpaRepository<DemandeDons, CleDemandeDons> {
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
            "where p.cod_soc = :codSoc\n" +
            "and nvl(TYP_AFF,'S') = 'A'\n" +
            "order by to_number(p.mat_pers)",nativeQuery = true)
    List<DemandeDonsPers> getPersInd(@Param("codSoc") String codSoc);

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
            "where p.cod_soc = :codSoc\n" +
            "and nvl(TYP_AFF,'S') = 'A' and p.cod_lieu_geog=:aff\n" +
            "order by to_number(p.mat_pers)",nativeQuery = true)
    List<DemandeDonsPers> getPersIndAffect(@Param("codSoc") String codSoc,@Param("aff") String aff);
    @Query(value = "select num_fam,NOM_PREN from famille \n" +
            "where cod_soc =:codSoc\n" +
            "and mat_pers = :matPers\n" ,nativeQuery = true)
    List<FamilleProjection> getFamille(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value = "select num_fam,NOM_PREN from famille \n" +
            "where cod_soc =:codSoc\n" +
            "and mat_pers = :matPers\n" +
            "and num_fam=0",nativeQuery = true)
    List<FamilleProjection> getFamAdherent(@Param("codSoc") String codSoc, @Param("matPers") String matPers);

    @Query(value = "select num_fam, NOM_PREN from famille " +
            "where cod_soc = :codSoc " +
            "and mat_pers = :matPers " +
            "and nvl(pec,'N') = 'O' " +
            "union " +
            "select num_fam, NOM_PREN from famille " +
            "where cod_soc = :codSoc " +
            "and mat_pers = :matPers " +
            "and nvl(pec_mut,'N') = 'O' " +
            "and nom_pren is not null " +
            "union " +
            "select 0, 'adhérent' from dual",
            nativeQuery = true)
    List<FamilleProjection> getFameEnfant(@Param("codSoc") String codSoc,
                                          @Param("matPers") String matPers);
    @Query(value = "select num_fam,NOM_PREN from famille \n" +
            "where cod_soc =:codSoc\n" +
            "and mat_pers = :matPers\n" +
            "and num_fam=99",nativeQuery = true)
    List<FamilleProjection> getFamConjoint(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value = "select typ_don,\n" +
            "       cod_soc,\n" +
            "     mat_pers,\n" +
            "        ( select  p.nom_pers || ' ' || p.pren_pers from personnel p where p.cod_soc=d.cod_soc and p.mat_pers=d.mat_pers  ) nom_pers,\n" +
            "       num_fam,\n" +
            "       dat_dem_don,\n" +
            "       nat_don,\n" +
            "       ( select lib_nat_don from NATURE_DON n where n.nat_don=d.nat_don  ) lib_nat_don,\n" +
            "       mnt_dem_don,\n" +
            "       raison,\n" +
            "       etat_dem,\n" +
            "       mnt_acc_don,\n" +
            "       etat_act,\n" +
            "       cod_lieu_geog,\n" +
            "       corps,\n" +
            "       cod_typ_depart,\n" +
            "       cod_affect,\n" +
            "       dat_debut,\n" +
            "       cod_dest,\n" +
            "       cod_fond,\n" +
            "       dat_deblocage,\n" +
            "       mode_payement,\n" +
            "       ref_payement,\n" +
            "       num_piece,\n" +
            "       rib,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       typ_benificiaire,\n" +
            "       mnt_livre,\n" +
            "       seq_ecrt,\n" +
            "       imput_cpt,\n" +
            "       ref_metier,\n" +
            "       num_vir\n" +
            "  from demande_dons d where cod_soc = :codSoc \n" +
            "  and typ_don ='I'\n" +
            "  and ETAT_DEM = 'I' and cod_lieu_geog = NVL(:affectation, cod_lieu_geog) \n" +
            "  and nat_don = nvl(:natDon, nat_don) order by to_number(mat_pers),dat_dem_don",nativeQuery = true)
    List<DemandeDonProjection> getDemandeVal(@Param("codSoc") String codSoc, @Param("affectation") String affectation, @Param("natDon") String natDon);
    @Query(value = "SELECT \n" +
            "    d.typ_don,\n" +
            "    d.cod_soc,\n" +
            "    d.mat_pers,\n" +
            "    (\n" +
            "        SELECT p.nom_pers || ' ' || p.pren_pers\n" +
            "        FROM personnel p\n" +
            "        WHERE p.cod_soc = d.cod_soc AND p.mat_pers = d.mat_pers\n" +
            "    ) AS nom_pers,\n" +
            "    d.num_fam,\n" +
            "    d.dat_dem_don,\n" +
            "    d.nat_don,\n" +
            "    (\n" +
            "        SELECT n.lib_nat_don\n" +
            "        FROM nature_don n\n" +
            "        WHERE n.nat_don = d.nat_don\n" +
            "    ) AS lib_nat_don,\n" +
            "    d.mnt_dem_don,\n" +
            "    d.raison,\n" +
            "    d.etat_dem,\n" +
            "    d.mnt_acc_don,\n" +
            "    d.etat_act,\n" +
            "    d.cod_lieu_geog,\n" +
            "    d.corps,\n" +
            "    d.cod_typ_depart,\n" +
            "    d.cod_affect,\n" +
            "    d.dat_debut,\n" +
            "    d.cod_dest,\n" +
            "    d.cod_fond,\n" +
            "    d.dat_deblocage,\n" +
            "    d.mode_payement,\n" +
            "    d.ref_payement,\n" +
            "    d.num_piece,\n" +
            "    d.rib,\n" +
            "    d.cod_user,\n" +
            "    d.dat_saisie,\n" +
            "    d.typ_benificiaire,\n" +
            "    d.mnt_livre,\n" +
            "    d.seq_ecrt,\n" +
            "    d.imput_cpt,\n" +
            "    d.ref_metier,\n" +
            "    d.num_vir\n" +
            "FROM \n" +
            "    demande_dons d\n" +
            "WHERE cod_soc = :codSoc \n" +
            "  AND d.mat_pers = :matPers\n" +
            "ORDER BY d.dat_dem_don ASC", nativeQuery = true)
    List<DemDonsProjection> getPersIndDemande(@Param("codSoc") String codSoc, @Param("matPers") String matPers);
    @Query(value = "select typ_don,\n" +
            "       cod_soc,\n" +
            "     mat_pers,\n" +
            "        ( select  p.nom_pers || ' ' || p.pren_pers from personnel p where p.cod_soc=d.cod_soc and p.mat_pers=d.mat_pers  ) nom_pers,\n" +
            "       num_fam,\n" +
            "       dat_dem_don,\n" +
            "       nat_don,\n" +
            "       ( select lib_nat_don from NATURE_DON n where n.nat_don=d.nat_don  ) lib_nat_don,\n" +
            "       mnt_dem_don,\n" +
            "       raison,\n" +
            "       etat_dem,\n" +
            "       mnt_acc_don,\n" +
            "       etat_act,\n" +
            "       cod_lieu_geog,\n" +
            "       corps,\n" +
            "       cod_typ_depart,\n" +
            "       cod_affect,\n" +
            "       dat_debut,\n" +
            "       cod_dest,\n" +
            "       cod_fond,\n" +
            "       dat_deblocage,\n" +
            "       mode_payement,\n" +
            "       ref_payement,\n" +
            "       num_piece,\n" +
            "       rib,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       typ_benificiaire,\n" +
            "       mnt_livre,\n" +
            "       seq_ecrt,\n" +
            "       imput_cpt,\n" +
            "       ref_metier,\n" +
            "       num_vir\n" +
            "  from demande_dons d where cod_soc = :codSoc and typ_don = 'I' and cod_lieu_geog=nvl(:affectation,cod_lieu_geog) and nat_don=nvl(:natDon,nat_don) and etat_dem='I'",nativeQuery = true)
    List<DemandeDonProjection> getDemandeIndScol(@Param("codSoc") String codSoc, @Param("affectation") String affectation, @Param("natDon") String natDon);
    @Query(value = "select typ_don,\n" +
            "       cod_soc,\n" +
            "       mat_pers,\n" +
            "       (select p.nom_pers || ' ' || p.pren_pers\n" +
            "          from personnel p\n" +
            "         where p.cod_soc = d.cod_soc\n" +
            "           and p.mat_pers = d.mat_pers) nom_pers,\n" +
            "       num_fam,\n" +
            "       dat_dem_don,\n" +
            "       nat_don,\n" +
            "       (select lib_nat_don from NATURE_DON where nat_don = d.nat_don) lib_nat,\n" +
            "       mnt_dem_don,\n" +
            "       raison,\n" +
            "       etat_dem,\n" +
            "       mnt_acc_don,\n" +
            "       etat_act,\n" +
            "       cod_lieu_geog,\n" +
            "       corps,\n" +
            "       cod_typ_depart,\n" +
            "       cod_affect,\n" +
            "       dat_debut,\n" +
            "       cod_dest,\n" +
            "       cod_fond,\n" +
            "       dat_deblocage,\n" +
            "       mode_payement,\n" +
            "       ref_payement,\n" +
            "       num_piece,\n" +
            "       rib,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       typ_benificiaire,\n" +
            "       mnt_livre,\n" +
            "       seq_ecrt,\n" +
            "       imput_cpt,\n" +
            "       ref_metier,\n" +
            "       num_vir\n" +
            "  from demande_dons d\n" +
            " where cod_soc = :codSoc\n" +
            "   and etat_dem = 'V'\n" +
            "   and cod_lieu_geog = nvl(:affectation, cod_lieu_geog)\n" +
            "   and nat_don = nvl(:natDon, nat_don)\n" +
            "   and dat_dem_don between nvl(TO_DATE(:datDeb, 'DD/MM/YYYY'), dat_dem_don)\n" +
            "                        and nvl(TO_DATE(:datFin, 'DD/MM/YYYY'), dat_dem_don)\n" +
            "   and dat_deblocage is null\n" +
            "   and mode_payement = nvl(:modPay, mode_payement)\n" +
            " order by mat_pers, dat_dem_don",
            nativeQuery = true)
    List<DemandeDonProjection> getDemandeDeblocage(
            @Param("codSoc") String codSoc,
            @Param("affectation") String affectation,
            @Param("natDon") String natDon,
            @Param("datDeb") String datDeb,
            @Param("datFin") String datFin,
            @Param("modPay") String modPay
    );


    @Query(value = "select typ_don,\n" +
            "       cod_soc,\n" +
            "       mat_pers,\n" +
            "       (select p.nom_pers || ' ' || p.pren_pers\n" +
            "          from personnel p\n" +
            "         where p.cod_soc = d.cod_soc\n" +
            "           and p.mat_pers = d.mat_pers) nom_pers,\n" +
            "       num_fam,\n" +
            "       dat_dem_don,\n" +
            "       nat_don,\n" +
            "       (select lib_nat_don from NATURE_DON where nat_don = d.nat_don) lib_nat,\n" +
            "       mnt_dem_don,\n" +
            "       raison,\n" +
            "       etat_dem,\n" +
            "       mnt_acc_don,\n" +
            "       etat_act,\n" +
            "       cod_lieu_geog,\n" +
            "       corps,\n" +
            "       cod_typ_depart,\n" +
            "       cod_affect,\n" +
            "       dat_debut,\n" +
            "       cod_dest,\n" +
            "       cod_fond,\n" +
            "       dat_deblocage,\n" +
            "       mode_payement,\n" +
            "       ref_payement,\n" +
            "       num_piece,\n" +
            "       rib,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       typ_benificiaire,\n" +
            "       mnt_livre,\n" +
            "       seq_ecrt,\n" +
            "       imput_cpt,\n" +
            "       ref_metier,\n" +
            "       num_vir\n" +
            "  from demande_dons d\n" +
            "  where cod_soc = :codSoc and etat_dem='V' and MODE_PAYEMENT='V' and cod_lieu_geog=nvl(:affectation,cod_lieu_geog) and nat_don=nvl(:natDon,nat_don)\n" +
            "and dat_deblocage = :datDebloc " +
            "   order by mat_pers,dat_dem_don",nativeQuery = true)
    List<DemandeDonProjection> getPersIndVirementScol(@Param("codSoc") String codSoc
            , @Param("datDebloc") String datDebloc
            , @Param("affectation") String affectation
            , @Param("natDon") String natDon

    );

    @Query(value = "select typ_don,\n" +
            "       cod_soc,\n" +
            "       mat_pers,\n" +
            "       (select p.nom_pers || ' ' || p.pren_pers\n" +
            "          from personnel p\n" +
            "         where p.cod_soc = d.cod_soc\n" +
            "           and p.mat_pers = d.mat_pers) nom_pers,\n" +
            "       num_fam,\n" +
            "       dat_dem_don,\n" +
            "       nat_don,\n" +
            "       (select lib_nat_don from NATURE_DON where nat_don = d.nat_don) lib_nat,\n" +
            "       mnt_dem_don,\n" +
            "       raison,\n" +
            "       etat_dem,\n" +
            "       mnt_acc_don,\n" +
            "       etat_act,\n" +
            "       cod_lieu_geog,\n" +
            "       corps,\n" +
            "       cod_typ_depart,\n" +
            "       cod_affect,\n" +
            "       dat_debut,\n" +
            "       cod_dest,\n" +
            "       cod_fond,\n" +
            "       dat_deblocage,\n" +
            "       mode_payement,\n" +
            "       ref_payement,\n" +
            "       num_piece,\n" +
            "       rib,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       typ_benificiaire,\n" +
            "       mnt_livre,\n" +
            "       seq_ecrt,\n" +
            "       imput_cpt,\n" +
            "       ref_metier,\n" +
            "       num_vir\n" +
            "  from demande_dons d\n" +
            "  where cod_soc = :codSoc and etat_dem='V' and cod_lieu_geog=nvl(:affectation,cod_lieu_geog) and nat_don=nvl(:natDon,nat_don)\n" +
            "and dat_deblocage = :datDebloc ",nativeQuery = true)
    List<DemandeDonProjection> getPersIndVirementScolCloture(@Param("codSoc") String codSoc
            , @Param("datDebloc") String datDebloc
            , @Param("affectation") String affectation
            , @Param("natDon") String natDon

    );


    @Query(value = "select typ_don,\n" +
            "       cod_soc,\n" +
            "     mat_pers,\n" +
            "        ( select  p.nom_pers || ' ' || p.pren_pers from personnel p where p.cod_soc=d.cod_soc and p.mat_pers=d.mat_pers  ) nom_pers,\n" +
            "       num_fam,\n" +
            "       dat_dem_don,\n" +
            "       nat_don,\n" +
            "       ( select lib_nat_don from NATURE_DON n where n.nat_don=d.nat_don  ) lib_nat_don,\n" +
            "       mnt_dem_don,\n" +
            "       raison,\n" +
            "       etat_dem,\n" +
            "       mnt_acc_don,\n" +
            "       etat_act,\n" +
            "       cod_lieu_geog,\n" +
            "       corps,\n" +
            "       cod_typ_depart,\n" +
            "       cod_affect,\n" +
            "       dat_debut,\n" +
            "       cod_dest,\n" +
            "       cod_fond,\n" +
            "       dat_deblocage,\n" +
            "       mode_payement,\n" +
            "       ref_payement,\n" +
            "       num_piece,\n" +
            "       rib,\n" +
            "       cod_user,\n" +
            "       dat_saisie,\n" +
            "       typ_benificiaire,\n" +
            "       mnt_livre,\n" +
            "       seq_ecrt,\n" +
            "       imput_cpt,\n" +
            "       ref_metier,\n" +
            "       num_vir\n" +
            "  from demande_dons d " +
            "where cod_soc = :codSoc " +
            "and typ_don = 'I' " +
            "and cod_lieu_geog=nvl(:affectation,cod_lieu_geog) " +
            "and dat_dem_don between nvl(TO_DATE(:datDeb, 'DD/MM/YYYY'), dat_dem_don)\n" +
            "                and nvl(TO_DATE(:datFin, 'DD/MM/YYYY'), dat_dem_don)\n" +
            "and nat_don=nvl(:natDon,nat_don) and etat_dem='I'",nativeQuery = true)
    List<DemandeDonProjection> PersIndValidationCp(@Param("codSoc") String codSoc,
                                                 @Param("affectation") String affectation,
                                                 @Param("natDon") String natDon,
                                                 @Param("datDeb") String datDeb,
                                                 @Param("datFin") String datFin);
}
