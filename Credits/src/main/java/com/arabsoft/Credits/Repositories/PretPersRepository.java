package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.ClePretPers;
import com.arabsoft.Credits.Entities.PretPers;
import com.arabsoft.Credits.Projections.DetailPretProjection;
import com.arabsoft.Credits.Projections.PretAnticipProjection;
import com.arabsoft.Credits.Projections.PretPErsProjection;
import com.arabsoft.Credits.Projections.PretReechProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Pageable;

 import java.util.List;

public interface PretPersRepository extends JpaRepository<PretPers, ClePretPers> {

    @Query(value="SELECT \n" +
            "    p.cod_grp_pret,\n" +
            "    p.typ_pret,\n" +
            "    t.lib_pret, t.nbr_tranche,\n" +
            "    t.plafond,\n" +
            "    NVL(t.taux_int, 0) AS taux_int,\n" +
            "    NVL(t.DUREE_REMB, 0) AS duree_remb,\n" +
            "    NVL(t.DELAI_GRACE, 0) AS delai_grace\n" +
            "FROM \n" +
            "    type_pret t\n" +
            "JOIN \n" +
            "    position_pret p \n" +
            "    ON p.cod_soc = t.cod_soc \n" +
            "    AND p.cod_grp_pret = t.cod_grp_pret \n" +
            "    AND p.typ_pret = t.typ_pret\n" +
            "JOIN \n" +
            "    groupe_pret g \n" +
            "    ON g.cod_soc = t.cod_soc \n" +
            "    AND g.cod_grp_pret = t.cod_grp_pret\n" +
            "WHERE \n" +
            "    p.cod_soc =:soc\n" +
            "    AND g.TYP_GROUPE IN ('P')\n" +
            "    AND t.libre_serv = 'O'\n" +
            "ORDER BY \n" +
            "    p.cod_grp_pret, \n" +
            "    p.typ_pret\n",nativeQuery = true)
    List<DetailPretProjection> getDetailsPret(@Param("soc") String soc);


    @Query(value="select p.cod_pret,t.lib_pret\n" +
            "from pret_pers p,type_pret t\n" +
            "where p.cod_soc =:soc\n" +
            "and p.mat_pers =:mat\n" +
            "and p.cod_etat_pret ='D'\n" +
            "and p.cod_grp_pret = t.cod_grp_pret\n" +
            "and p.typ_pret = t.typ_pret\n" +
            "order by p.cod_pret",nativeQuery = true)
    List<PretReechProjection> getDetailPretReech(@Param("soc") String soc,@Param("mat") String mat);
    @Query(value="select distinct p.cod_grp_pret codGrpPret,t.lib_grp_pret libPret \n" +
            "from pret_pers p,groupe_pret t\n" +
            "where p.cod_soc=:soc \n" +
            "and p.cod_grp_pret =  t.cod_grp_pret ",nativeQuery = true)
    List<DetailPretProjection> getGroupePret(@Param("soc") String soc);
    @Query(value="select nvl(max(cod_pret),0)+1 from pret_pers where cod_soc=:soc and mat_pers=:mat ",nativeQuery = true)
    Long getMaxCodPret(@Param("soc")String soc,@Param("mat")String mat);

    @Query(value = "select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_pret,\n" +
            "       t.num_dem_pret,\n" +
            "       t.prt_dat_dem,\n" +
            "       t.prt_mnt_dem,\n" +
            "       t.cod_grp_pret,\n" +
            "       t.typ_pret,\n" +
            "       t.cod_etat_pret,\n" +
            "       t.typ_etat,\n" +
            "       t.org_pret,\n" +
            "       t.mod_remb,\n" +
            "       t.methode_calc,\n" +
            "       t.cod_dept_pers,\n" +
            "       t.cod_serv_pers,\n" +
            "       t.cod_motif_pers,\n" +
            "       t.cod_categ_pers,\n" +
            "       t.cod_cat_pers,\n" +
            "       t.cod_grad_pers,\n" +
            "       t.adm_tech,\n" +
            "       t.cod_affect,\n" +
            "       t.cod_lieu_geog,\n" +
            "       t.corps,\n" +
            "       t.cod_typ_depart,\n" +
            "       t.nbr_enf,\n" +
            "       t.prt_dat_acc,\n" +
            "       t.prt_dat_deb,\n" +
            "       t.prt_dat_fin,\n" +
            "       t.prt_ech,\n" +
            "       t.prt_taux,\n" +
            "       t.prt_mnt_glb,\n" +
            "       t.prt_mnt_prm,\n" +
            "       t.delai_grace,\n" +
            "       t.prt_int_grace,\n" +
            "       t.prt_mnt_rem,\n" +
            "       t.rem_men,\n" +
            "       t.dern_rem_men,\n" +
            "       t.prt_rendu,\n" +
            "       t.prt_rendu_int,\n" +
            "       t.nbr_retenue,\n" +
            "       t.dat_deblocage,\n" +
            "       t.dat_comptable,\n" +
            "       t.mode_reglement,\n" +
            "       t.piece_reglement,\n" +
            "       t.mnt_report,\n" +
            "       t.cod_pret_ant,\n" +
            "       t.ass_pret,\n" +
            "       t.objet_pret,\n" +
            "       t.dat_saisie,\n" +
            "       t.cod_user,\n" +
            "       t.cod_motif_susp,\n" +
            "       t.cod_pret_ref,\n" +
            "       t.num_etat_pret,\n" +
            "       t.amort_pret,\n" +
            "       t.nbr_tranche,\n" +
            "       t.dat_effet,\n" +
            "       t.prt_mnt_debloque,\n" +
            "       t.prt_int_tranch,\n" +
            "       t.num_lig_dem_pret,\n" +
            "       t.prt_interet,\n" +
            "       t.num_comm,\n" +
            "       t.seq_ecrt,\n" +
            "       t.num_vir,\n" +

            "       (select p.Nom_Pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers and t.cod_soc=p.cod_soc)nomPers from pret_pers t where cod_soc = :soc and mat_pers=:mat order by cod_pret desc", nativeQuery = true)
    List<PretPErsProjection> getPretEnCours(@Param("soc") String soc,@Param("mat") String mat);

    @Query(value = "SELECT t.*, " +
            "       (p.Nom_Pers || ' ' || p.pren_pers) as nomPers,p.num_retr " +
            "FROM pret_pers t " +
            "LEFT JOIN personnel p ON t.mat_pers = p.mat_pers AND t.cod_soc = p.cod_soc " +
            "WHERE t.cod_soc = :soc " +
            "  AND (:mat IS NULL OR :mat = '' OR t.mat_pers = :mat) " +
            "  AND (:numRetr IS NULL OR :numRetr = '' OR p.num_retr = :numRetr) " +
            "  AND (UPPER(p.Nom_Pers || ' ' || p.pren_pers) LIKE UPPER('%' || :nom || '%')) " +
            "  AND (:numComm IS NULL OR :numComm = '' OR t.num_comm =:numComm ) " +
            "  AND (:etat IS NULL OR :etat = '' OR t.cod_etat_pret = :etat) " +
            "  AND (:codPret IS NULL OR :codPret = '' OR t.cod_pret =:codPret) " + // Added this
            "  AND (:modRemb IS NULL OR :modRemb = '' OR t.mod_remb = :modRemb ) " + // Added this
            "  AND (:datSaisie IS NULL OR :datSaisie = '' OR TO_CHAR(t.dat_saisie, 'YYYY-MM-DD') = :datSaisie) "  + // Added this

            "ORDER BY t.cod_pret DESC", nativeQuery = true)
    List<PretPErsProjection> getPretEnCoursLOV(
            @Param("soc") String soc,
            @Param("mat") String mat,
            @Param("nom") String nom,
            @Param("numComm") String numComm,
            @Param("etat") String etat,
            @Param("codPret") String codPret, // Added this
            @Param("modRemb") String modRemb,
            @Param("datSaisie") String datSaisie,
            @Param("numRetr") String numRetr

    );
    @Query(value = "select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_pret,\n" +
            "       t.num_dem_pret,\n" +
            "       t.prt_dat_dem,\n" +
            "       t.prt_mnt_dem,\n" +
            "       t.cod_grp_pret,\n" +
            "       t.typ_pret,\n" +
            "       t.cod_etat_pret,\n" +
            "       t.typ_etat,\n" +
            "       t.org_pret,\n" +
            "       t.mod_remb,\n" +
            "       t.methode_calc,\n" +
            "       t.cod_dept_pers,\n" +
            "       t.cod_serv_pers,\n" +
            "       t.cod_motif_pers,\n" +
            "       t.cod_categ_pers,\n" +
            "       t.cod_cat_pers,\n" +
            "       t.cod_grad_pers,\n" +
            "       t.adm_tech,\n" +
            "       t.cod_affect,\n" +
            "       t.cod_lieu_geog,\n" +
            "       t.corps,\n" +
            "       t.cod_typ_depart,\n" +
            "       t.nbr_enf,\n" +
            "       t.prt_dat_acc,\n" +
            "       t.prt_dat_deb,\n" +
            "       t.prt_dat_fin,\n" +
            "       t.prt_ech,\n" +
            "       t.prt_taux,\n" +
            "       t.prt_mnt_glb,\n" +
            "       t.prt_mnt_prm,\n" +
            "       t.delai_grace,\n" +
            "       t.prt_int_grace,\n" +
            "       t.prt_mnt_rem,\n" +
            "       t.rem_men,\n" +
            "       t.dern_rem_men,\n" +
            "       t.prt_rendu,\n" +
            "       t.prt_rendu_int,\n" +
            "       t.nbr_retenue,\n" +
            "       t.dat_deblocage,\n" +
            "       t.dat_comptable,\n" +
            "       t.mode_reglement,\n" +
            "       t.piece_reglement,\n" +
            "       t.mnt_report,\n" +
            "       t.cod_pret_ant,\n" +
            "       t.ass_pret,\n" +
            "       t.objet_pret,\n" +
            "       t.dat_saisie,\n" +
            "       t.cod_user,\n" +
            "       t.cod_motif_susp,\n" +
            "       t.cod_pret_ref,\n" +
            "       t.num_etat_pret,\n" +
            "       t.amort_pret,\n" +
            "       t.nbr_tranche,\n" +
            "       t.dat_effet,\n" +
            "       t.prt_mnt_debloque,\n" +
            "       t.prt_int_tranch,\n" +
            "       t.num_lig_dem_pret,\n" +
            "       t.prt_interet,\n" +
            "       t.num_comm,\n" +
            "       t.seq_ecrt,\n" +
            "       t.num_vir,\n" +
            "       (select p.Nom_Pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers and t.cod_soc=p.cod_soc)nomPers from pret_pers t where cod_soc = :soc  and cod_etat_pret IN  ('S')  order by t.mat_pers,cod_pret desc", nativeQuery = true)
    List<PretPErsProjection> getPretValid(@Param("soc") String soc);
    @Query(value = "select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_pret,\n" +
            "       t.num_dem_pret,\n" +
            "       t.prt_dat_dem,\n" +
            "       t.prt_mnt_dem,\n" +
            "       t.cod_grp_pret,\n" +
            "       t.typ_pret,\n" +
            "       t.cod_etat_pret,\n" +
            "       t.typ_etat,\n" +
            "       t.org_pret,\n" +
            "       t.mod_remb,\n" +
            "       t.methode_calc,\n" +
            "       t.cod_dept_pers,\n" +
            "       t.cod_serv_pers,\n" +
            "       t.cod_motif_pers,\n" +
            "       t.cod_categ_pers,\n" +
            "       t.cod_cat_pers,\n" +
            "       t.cod_grad_pers,\n" +
            "       t.adm_tech,\n" +
            "       t.cod_affect,\n" +
            "       t.cod_lieu_geog,\n" +
            "       t.corps,\n" +
            "       t.cod_typ_depart,\n" +
            "       t.nbr_enf,\n" +
            "       t.prt_dat_acc,\n" +
            "       t.prt_dat_deb,\n" +
            "       t.prt_dat_fin,\n" +
            "       t.prt_ech,\n" +
            "       t.prt_taux,\n" +
            "       t.prt_mnt_glb,\n" +
            "       t.prt_mnt_prm,\n" +
            "       t.delai_grace,\n" +
            "       t.prt_int_grace,\n" +
            "       t.prt_mnt_rem,\n" +
            "       t.rem_men,\n" +
            "       t.dern_rem_men,\n" +
            "       t.prt_rendu,\n" +
            "       t.prt_rendu_int,\n" +
            "       t.nbr_retenue,\n" +
            "       t.dat_deblocage,\n" +
            "       t.dat_comptable,\n" +
            "       t.mode_reglement,\n" +
            "       t.piece_reglement,\n" +
            "       t.mnt_report,\n" +
            "       t.cod_pret_ant,\n" +
            "       t.ass_pret,\n" +
            "       t.objet_pret,\n" +
            "       t.dat_saisie,\n" +
            "       t.cod_user,\n" +
            "       t.cod_motif_susp,\n" +
            "       t.cod_pret_ref,\n" +
            "       t.num_etat_pret,\n" +
            "       t.amort_pret,\n" +
            "       t.nbr_tranche,\n" +
            "       t.dat_effet,\n" +
            "       t.prt_mnt_debloque,\n" +
            "       t.prt_int_tranch,\n" +
            "       t.num_lig_dem_pret,\n" +
            "       t.prt_interet,\n" +
            "       t.num_comm,\n" +
            "       t.seq_ecrt,\n" +
            "       t.num_vir,\n" +
            "       (select p.Nom_Pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers and t.cod_soc=p.cod_soc)nomPers from pret_pers t where cod_soc = :soc  and cod_etat_pret IN  ('S','V')  order by t.mat_pers,cod_pret desc", nativeQuery = true)
    List<PretPErsProjection> getPretDelet(@Param("soc") String soc);
    @Query(value = "select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_pret,\n" +
            "       t.num_dem_pret,\n" +
            "       t.prt_dat_dem,\n" +
            "       t.prt_mnt_dem,\n" +
            "       t.cod_grp_pret,\n" +
            "       t.typ_pret,\n" +
            "       t.cod_etat_pret,\n" +
            "       t.typ_etat,\n" +
            "       t.org_pret,\n" +
            "       t.mod_remb,\n" +
            "       t.methode_calc,\n" +
            "       t.cod_dept_pers,\n" +
            "       t.cod_serv_pers,\n" +
            "       t.cod_motif_pers,\n" +
            "       t.cod_categ_pers,\n" +
            "       t.cod_cat_pers,\n" +
            "       t.cod_grad_pers,\n" +
            "       t.adm_tech,\n" +
            "       t.cod_affect,\n" +
            "       t.cod_lieu_geog,\n" +
            "       t.corps,\n" +
            "       t.cod_typ_depart,\n" +
            "       t.nbr_enf,\n" +
            "       t.prt_dat_acc,\n" +
            "       t.prt_dat_deb,\n" +
            "       t.prt_dat_fin,\n" +
            "       t.prt_ech,\n" +
            "       t.prt_taux,\n" +
            "       t.prt_mnt_glb,\n" +
            "       t.prt_mnt_prm,\n" +
            "       t.delai_grace,\n" +
            "       t.prt_int_grace,\n" +
            "       t.prt_mnt_rem,\n" +
            "       t.rem_men,\n" +
            "       t.dern_rem_men,\n" +
            "       t.prt_rendu,\n" +
            "       t.prt_rendu_int,\n" +
            "       t.nbr_retenue,\n" +
            "       t.dat_deblocage,\n" +
            "       t.dat_comptable,\n" +
            "       t.mode_reglement,\n" +
            "       t.piece_reglement,\n" +
            "       t.mnt_report,\n" +
            "       t.cod_pret_ant,\n" +
            "       t.ass_pret,\n" +
            "       t.objet_pret,\n" +
            "       t.dat_saisie,\n" +
            "       t.cod_user,\n" +
            "       t.cod_motif_susp,\n" +
            "       t.cod_pret_ref,\n" +
            "       t.num_etat_pret,\n" +
            "       t.amort_pret,\n" +
            "       t.nbr_tranche,\n" +
            "       t.dat_effet,\n" +
            "       t.prt_mnt_debloque,\n" +
            "       t.prt_int_tranch,\n" +
            "       t.num_lig_dem_pret,\n" +
            "       t.prt_interet,\n" +
            "       t.num_comm,\n" +
            "       t.seq_ecrt,\n" +
            "       t.num_vir,\n" +
            "       (select p.Nom_Pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers and t.cod_soc=p.cod_soc)nomPers from pret_pers t where cod_soc = :soc  and  cod_etat_pret = 'V' order by cod_pret desc", nativeQuery = true)
    List<PretPErsProjection> getPretDebl(@Param("soc") String soc);

    @Query(value = "select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_pret,\n" +
            "       t.num_dem_pret,\n" +
            "       t.prt_dat_dem,\n" +
            "       t.prt_mnt_dem,\n" +
            "       t.cod_grp_pret,\n" +
            "       t.typ_pret,\n" +
            "       t.cod_etat_pret,\n" +
            "       t.typ_etat,\n" +
            "       t.org_pret,\n" +
            "       t.mod_remb,\n" +
            "       t.methode_calc,\n" +
            "       t.cod_dept_pers,\n" +
            "       t.cod_serv_pers,\n" +
            "       t.cod_motif_pers,\n" +
            "       t.cod_categ_pers,\n" +
            "       t.cod_cat_pers,\n" +
            "       t.cod_grad_pers,\n" +
            "       t.adm_tech,\n" +
            "       t.cod_affect,\n" +
            "       t.cod_lieu_geog,\n" +
            "       t.corps,\n" +
            "       t.cod_typ_depart,\n" +
            "       t.nbr_enf,\n" +
            "       t.prt_dat_acc,\n" +
            "       t.prt_dat_deb,\n" +
            "       t.prt_dat_fin,\n" +
            "       t.prt_ech,\n" +
            "       t.prt_taux,\n" +
            "       t.prt_mnt_glb,\n" +
            "       t.prt_mnt_prm,\n" +
            "       t.delai_grace,\n" +
            "       t.prt_int_grace,\n" +
            "       t.prt_mnt_rem,\n" +
            "       t.rem_men,\n" +
            "       t.dern_rem_men,\n" +
            "       t.prt_rendu,\n" +
            "       t.prt_rendu_int,\n" +
            "       t.nbr_retenue,\n" +
            "       t.dat_deblocage,\n" +
            "       t.dat_comptable,\n" +
            "       t.mode_reglement,\n" +
            "       t.piece_reglement,\n" +
            "       t.mnt_report,\n" +
            "       t.cod_pret_ant,\n" +
            "       t.ass_pret,\n" +
            "       t.objet_pret,\n" +
            "       t.dat_saisie,\n" +
            "       t.cod_user,\n" +
            "       t.cod_motif_susp,\n" +
            "       t.cod_pret_ref,\n" +
            "       t.num_etat_pret,\n" +
            "       t.amort_pret,\n" +
            "       t.nbr_tranche,\n" +
            "       t.dat_effet,\n" +
            "       t.prt_mnt_debloque,\n" +
            "       t.prt_int_tranch,\n" +
            "       t.num_lig_dem_pret,\n" +
            "       t.prt_interet,\n" +
            "       t.num_comm,\n" +
            "       t.seq_ecrt,\n" +
            "       t.num_vir,\n" +
            "       (select p.Nom_Pers ||' '|| p.pren_pers from personnel p where p.mat_pers=t.mat_pers and t.cod_soc=p.cod_soc)nomPers " +
            "from pret_pers t where cod_soc =:soc and COD_GRP_PRET in (select COD_GRP_PRET from groupe_pret \n" +
            "where TYP_GROUPE in ('P'))\n" +
            "and cod_etat_pret ='RE' order by cod_pret desc", nativeQuery = true)
    List<PretPErsProjection> getPretReech(@Param("soc") String soc);
    @Query(value = "select * from pret_pers", nativeQuery = true)
    List<PretPers> getPretEnCourss();


    @Query(value="select p.cod_pret,\n" +
            "       p.typ_pret,\n" +
            "       lib_pret,\n" +
            "       TO_CHAR(prt_mnt_rem,'9999999999.000') AS  prt_mnt_rem,\n" +
            "       p.prt_ech,\n" +
            "      TO_CHAR(prt_rendu,'9999999999.000') prt_rendu,\n" +
            "      TO_CHAR(rem_men,'9999999999.000') rem_men,\n" +
            "       p.cod_grp_pret,\n" +
            "       to_char(p.prt_dat_deb, 'mm/yyyy') prt_dat_deb,\n" +
            "       to_char(p.prt_dat_fin, 'mm/yyyy') prt_dat_fin,\n" +
            "       p.nbr_retenue,\n" +
            "       nvl(DAT_EFFET, prt_dat_deb)dat_effet\n" +
            "  from type_pret t, pret_pers p, groupe_pret g\n" +
            " where p.cod_soc =:soc\n" +
            "   and p.mat_pers =:mat\n" +
            "   and p.typ_pret = t.typ_pret\n" +
            "   and p.cod_grp_pret = g.cod_grp_pret\n" +
            "   and p.cod_grp_pret = t.cod_grp_pret\n" +
            "   and g.typ_groupe = 'P'\n" +
            "   and p.cod_etat_pret = 'D'\n" +
            " order by p.cod_pret\n",nativeQuery = true)
    List<PretAnticipProjection> getPretAnticip(@Param("soc")String soc, @Param("mat") String mat);

    @Query(value="select p.cod_pret,\n" +
            "       p.typ_pret,\n" +
            "       lib_pret,\n" +
            "       TO_CHAR(prt_mnt_rem,'9999999999.000') AS  prt_mnt_rem,\n" +
            "       p.prt_ech,\n" +
            "      TO_CHAR(prt_rendu,'9999999999.000') prt_rendu,\n" +
            "      TO_CHAR(rem_men,'9999999999.000') rem_men,\n" +
            "       p.cod_grp_pret,\n" +
            "       to_char(p.prt_dat_deb, 'mm/yyyy') prt_dat_deb,\n" +
            "       to_char(p.prt_dat_fin, 'mm/yyyy') prt_dat_fin,\n" +
            "       p.nbr_retenue,\n" +
            "       nvl(DAT_EFFET, prt_dat_deb)dat_effet\n" +
            "  from type_pret t, pret_pers p, groupe_pret g\n" +
            " where p.cod_soc =:soc\n" +
            "   and p.mat_pers =:mat\n" +
            "   and p.cod_pret =:codPret\n" +
            "   and p.typ_pret = t.typ_pret\n" +
            "   and p.cod_grp_pret = g.cod_grp_pret\n" +
            "   and p.cod_grp_pret = t.cod_grp_pret\n" +
            "   and g.typ_groupe = 'P'\n" +
             " order by p.cod_pret\n",nativeQuery = true)
    PretAnticipProjection getPretAnticipByPret(@Param("soc")String soc, @Param("mat") String mat,@Param("codPret") String codPret);
    @Query(value="select p.cod_pret,\n" +
            "       p.typ_pret,\n" +
            "       lib_pret,\n" +
            "       TO_CHAR(prt_mnt_glb, '9999999999.000') AS prt_mnt_glb,\n" +
            "       p.cod_grp_pret,\n" +
            "       to_char(p.prt_dat_deb, 'mm/yyyy') prt_dat_deb,\n" +
            "       to_char(p.prt_dat_fin, 'mm/yyyy') prt_dat_fin,\n" +
            "       p.nbr_retenue,\n" +
            "       nvl(DAT_EFFET, prt_dat_deb)dat_effet\n" +
            "  from type_pret t, pret_pers p, groupe_pret g\n" +
            " where p.cod_soc =:soc\n" +
            "   and p.mat_pers =:mat\n" +
            "   and p.mat_pers =:mat\n" +
            "   and p.cod_pret =:codPret \n"+
            "   and p.typ_pret = t.typ_pret\n" +
            "   and p.cod_grp_pret = g.cod_grp_pret\n" +
            "   and p.cod_grp_pret = t.cod_grp_pret\n" +
            "   and g.typ_groupe = 'P'\n" +
            "   and p.cod_etat_pret = 'D'\n" +
            " order by p.cod_pret\n",nativeQuery = true)
    PretAnticipProjection getPretAnticipDetails(@Param("soc")String soc, @Param("mat") String mat,@Param("codPret") String codPret);


    @Procedure(procedureName = "pk_gestion_credit.anticip_pret")
    void anticipPret(
            @Param("soc_") String soc,
            @Param("mat_") String mat,
            @Param("cod_") Long cod,
            @Param("cod_ant_") Long codAnt
    );
}
