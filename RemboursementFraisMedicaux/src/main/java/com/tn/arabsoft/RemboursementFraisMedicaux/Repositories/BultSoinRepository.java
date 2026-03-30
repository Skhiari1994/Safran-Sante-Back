package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.BultSoin;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.BultSoinCle;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BultSoinRepository extends JpaRepository<BultSoin, BultSoinCle> {

        @Query(value = "SELECT COUNT(*) FROM bult_soin " +
                        "WHERE COD_ASSUR = :codAssur " +
                        "AND NUM_SOIN = :numSoin", nativeQuery = true)
        Long getNbreBult(@Param("codAssur") String codAssur, @Param("numSoin") String numSoin);

        @Query(value = "select NUM_PEC,DAT_PEC,ETAB_RSOC||' '||PR_RSOC etablis \n" +
                        "from prise_charge p,ref_etablis r\n" +
                        "where cod_soc =:soc\n" +
                        "and mat_pers =:mat\n" +
                        "and num_fam =:numFam\n" +
                        "and p.prf_typ = r.prf_typ\n" +
                        "and p.prf_cod = r.prf_cod\n" +
                        "and dat_eff <=to_date(:datSoin,'dd/mm/yyyy')\n" +
                        "and etat_pec = 'V'\n", nativeQuery = true)
        List<PECProjection> getListEtablisPec(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Query(value = "select b.mat_pers matricule,b.NUM_ASSUR,nom_pers||' '||pren_pers nom,num_fam prestataire,dat_soin\n"
                        +
                        "from bult_soin b ,personnel p\n" +
                        "where b.cod_soc =:soc\n" +
                        "and p.cod_soc = b.cod_soc\n" +
                        "and p.mat_pers = b.mat_pers\n" +
                        "and b.NAT_BULT = 'M'\n" +
                        "and cod_bord is null\n" +
                        "and nvl(envoi,'N') = 'N'\n" +
                        "order by dat_soin DESC, b.mat_pers, num_fam", nativeQuery = true)
        List<PersConsProjection> getPersCons(@Param("soc") String soc);

        @Query(value = "SELECT \n" +
                        "    t.cod_soc,\n" +
                        "    t.mat_pers,\n" +
                        "    t.num_fam,\n" +
                        "    t.dat_soin,\n" +
                        "    t.cod_bord,\n" +
                        "    t.cod_assur,\n" +
                        "    t.num_soin,\n" +
                        "    t.ord_bult,\n" +
                        "    t.tot_honor,\n" +
                        "    t.tot_net,\n" +
                        "    t.tot_remb,\n" +
                        "    t.reg_remb,\n" +
                        "    t.cod_malad,\n" +
                        "    t.num_pec,\n" +
                        "    t.cod_fil,\n" +
                        "    t.num_assur,\n" +
                        "    t.dat_prev_accouch,\n" +
                        "    t.nat_bult,\n" +
                        "    t.mat_pers_conj,\n" +
                        "    t.num_ass_conj,\n" +
                        "    t.dat_saisie,\n" +
                        "    t.obs,\n" +
                        "    t.obs_a,\n" +
                        "    t.envoi,\n" +
                        "    t.ann_plaf_imp,\n" +
                        "    t.reg_adh,\n" +
                        "    t.mod_pay,\n" +
                        "    t.dat_vir,\n" +
                        "    p.mat_int,\n" +
                        "    p.dat_nais,\n" +
                        "    p.nom_pers || ' ' || p.pren_pers AS nomCompletPers,\n" +
                        "    a.lib_assur AS libAssur,\n" +
                        "    r.lib_fill AS libFill,\n" +
                        "    f.nom_pren AS nomAdherent\n" +
                        "FROM bult_soin t  \n" +
                        "LEFT JOIN personnel p ON p.mat_pers = t.mat_pers\n" +
                        "LEFT JOIN assurance a ON a.cod_assur = t.cod_assur\n" +
                        "LEFT JOIN ref_filliere r ON r.cod_fil = t.cod_fil\n" +
                        "LEFT JOIN famille f ON f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers where   NAT_BULT = 'M'\n"
                        +
                        "and nvl(envoi,'N') = 'N' order by dat_saisie DESC, dat_soin DESC, mat_pers, num_fam ", nativeQuery = true)
        List<BultSoinProjection> getBulletinSoin();

        @Query(value = "SELECT \n" +
                        "    t.cod_soc,\n" +
                        "    t.mat_pers,\n" +
                        "    t.num_fam,\n" +
                        "    t.dat_soin,\n" +
                        "    t.cod_bord,\n" +
                        "    t.cod_assur,\n" +
                        "    t.num_soin,\n" +
                        "    t.ord_bult,\n" +
                        "    t.tot_honor,\n" +
                        "    t.tot_net,\n" +
                        "    t.tot_remb,\n" +
                        "    t.reg_remb,\n" +
                        "    t.cod_malad,\n" +
                        "    t.num_pec,\n" +
                        "    t.cod_fil,\n" +
                        "    t.num_assur,\n" +
                        "    t.dat_prev_accouch,\n" +
                        "    t.nat_bult,\n" +
                        "    t.mat_pers_conj,\n" +
                        "    t.num_ass_conj,\n" +
                        "    t.dat_saisie,\n" +
                        "    t.obs,\n" +
                        "    t.obs_a,\n" +
                        "    t.envoi,\n" +
                        "    t.ann_plaf_imp,\n" +
                        "    t.reg_adh,\n" +
                        "    t.mod_pay,\n" +
                        "    t.dat_vir,\n" +
                        "    p.nom_pers || ' ' || p.pren_pers AS nom_complet_pers,\n" +
                        "    a.lib_assur AS lib_assur,\n" +
                        "    r.lib_fill AS lib_fill,\n" +
                        "    f.nom_pren AS nom_adherent\n" +
                        "FROM bult_soin t  \n" +
                        "LEFT JOIN personnel p ON p.mat_pers = t.mat_pers\n" +
                        "LEFT JOIN assurance a ON a.cod_assur = t.cod_assur\n" +
                        "LEFT JOIN ref_filliere r ON r.cod_fil = t.cod_fil\n" +
                        "LEFT JOIN famille f ON f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers\n" +
                        "WHERE NAT_BULT = 'M'\n" +
                        "AND NVL(envoi, 'N') = 'O'\n" +
                        "AND (:mat IS NULL OR t.mat_pers = :mat)\n" +
                        "AND (:num_fam IS NULL OR t.num_fam = :num_fam)\n" +
                        "AND (:dat_soin IS NULL OR t.dat_soin = :dat_soin)\n" +
                        "ORDER BY t.dat_saisie DESC, t.dat_soin DESC, t.mat_pers, t.num_fam \n", nativeQuery = true)
        List<BultSoinProjection> getBulletinSoinCons(@Param("mat") String mat, @Param("num_fam") String num_fam,
                        @Param("dat_soin") String dat_soin);

        @Query(value = "SELECT \n" +
                        "    t.cod_soc,\n" +
                        "    t.mat_pers,\n" +
                        "    t.num_fam,\n" +
                        "    t.dat_soin,\n" +
                        "    t.cod_bord,\n" +
                        "    t.cod_assur,\n" +
                        "    t.num_soin,\n" +
                        "    t.ord_bult,\n" +
                        "    t.tot_honor,\n" +
                        "    t.tot_net,\n" +
                        "    t.tot_remb,\n" +
                        "    t.reg_remb,\n" +
                        "    t.cod_malad,\n" +
                        "    t.num_pec,\n" +
                        "    t.cod_fil,\n" +
                        "    t.num_assur,\n" +
                        "    t.dat_prev_accouch,\n" +
                        "    t.nat_bult,\n" +
                        "    t.mat_pers_conj,\n" +
                        "    t.num_ass_conj,\n" +
                        "    t.dat_saisie,\n" +
                        "    t.obs,\n" +
                        "    t.obs_a,\n" +
                        "    t.envoi,\n" +
                        "    t.ann_plaf_imp,\n" +
                        "    t.reg_adh,\n" +
                        "    t.mod_pay,\n" +
                        "    t.dat_vir,\n" +
                        "    p.nom_pers || ' ' || p.pren_pers AS nom_complet_pers,\n" +
                        "    a.lib_assur AS lib_assur,\n" +
                        "    r.lib_fill AS lib_fill,\n" +
                        "    f.nom_pren AS nom_adherent\n" +
                        "FROM bult_soin t  \n" +
                        "LEFT JOIN personnel p ON p.mat_pers = t.mat_pers\n" +
                        "LEFT JOIN assurance a ON a.cod_assur = t.cod_assur\n" +
                        "LEFT JOIN ref_filliere r ON r.cod_fil = t.cod_fil\n" +
                        "LEFT JOIN famille f ON f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers\n" +
                        "WHERE NAT_BULT = 'C'\n" +
                        "AND NVL(envoi, 'N') = 'O'\n" +
                        "AND (:mat IS NULL OR t.mat_pers = :mat)\n" +
                        "AND (:num_fam IS NULL OR t.num_fam = :num_fam)\n" +
                        "AND (:dat_soin IS NULL OR t.dat_soin = :dat_soin)\n" +
                        "ORDER BY t.dat_saisie DESC, t.dat_soin DESC, t.mat_pers, t.num_fam \n", nativeQuery = true)
        List<BultSoinProjection> getBulletinCnamCons(@Param("mat") String mat, @Param("num_fam") String num_fam,
                        @Param("dat_soin") String dat_soin);

        @Query(value = "select b.ABRV_ACT,LIB_ACT\n" +
                        "from bareme_remb b ,acte a\n" +
                        "where cod_fil =:codFil \n" +
                        "and COD_ASSUR =:codAssur \n" +
                        "and b.abrv_act = a.abrv_act\n" +
                        "and nvl(a.parente,:parente) =:parente\n" +
                        "and nvl(a.sexe,:sexe) =:sexe\n" +
                        "and a.abrv_act not in (select abrv_act from ref_fill_act where cod_fil =:codFil)", nativeQuery = true)
        List<ActeProjection> getActe(@Param("codFil") String codFil, @Param("codAssur") String codAssur,
                        @Param("parente") String parente, @Param("sexe") String sexe);

        @Query(value = "SELECT " +
                        "t.cod_soc, " +
                        "t.mat_pers, " +
                        "t.num_fam, " +
                        "t.dat_soin, " +
                        "t.cod_bord, " +
                        "t.cod_assur, " +
                        "t.num_soin, " +
                        "t.ord_bult, " +
                        "t.tot_honor, " +
                        "t.tot_net, " +
                        "t.tot_remb, " +
                        "t.reg_remb, " +
                        "t.cod_malad, " +
                        "t.num_pec, " +
                        "t.cod_fil, " +
                        "t.num_assur, " +
                        "t.dat_prev_accouch, " +
                        "t.nat_bult, " +
                        "t.mat_pers_conj, " +
                        "t.num_ass_conj, " +
                        "t.dat_saisie, " +
                        "t.obs, " +
                        "t.obs_a, " +
                        "t.envoi, " +
                        "t.ann_plaf_imp, " +
                        "t.reg_adh, " +
                        "t.mod_pay, " +
                        "t.dat_vir, " +

                        "(select p.nom_pers || ' ' || p.pren_pers from personnel p where p.mat_pers=t.mat_pers) AS nom_complet_pers, "
                        +
                        "(select p.num_retr from personnel p where p.mat_pers=t.mat_pers AND ROWNUM = 1) AS num_retr, "
                        + // ADD THIS
                        "(select lib_assur from assurance a where a.cod_assur = t.cod_assur)AS lib_assur, " +
                        "(select lib_fill from ref_filliere r where r.cod_fil = t.cod_fil)AS lib_fill, " + // ADD THIS
                        "(select lib_remb FROM regime_remb rr WHERE rr.reg_remb = t.reg_remb) AS lib_remb, " + // ADD
                                                                                                               // THIS
                        "(select f.nom_pren from famille f where f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers)AS nom_adherent "
                        +
                        "FROM BULT_SOIN t " +
                        "WHERE t.cod_soc = :codSoc AND t.cod_assur = :codAssur AND t.nat_bult = 'C' AND t.cod_bord = :codBord "
                        +
                        "ORDER BY t.dat_saisie DESC, t.mat_pers, t.num_fam", nativeQuery = true)
        List<BultSoinProjection> getBulletinSoinEnvoi(@Param("codSoc") String codSoc, @Param("codBord") String codBord,
                        @Param("codAssur") String codAssur);

        @Query(value = "SELECT " +
                        "    t.cod_soc, " +
                        "    t.mat_pers, " +
                        "    t.num_fam, " +
                        "    t.dat_soin, " +
                        "    t.cod_bord, " +
                        "    t.cod_assur, " +
                        "    t.num_soin, " +
                        "    t.ord_bult, " +
                        "    t.tot_honor, " +
                        "    t.tot_net, " +
                        "    t.tot_remb, " +
                        "    t.reg_remb, " +
                        "    t.cod_malad, " +
                        "    t.num_pec, " +
                        "    t.cod_fil, " +
                        "    t.num_assur, " +
                        "    t.dat_prev_accouch, " +
                        "    t.nat_bult, " +
                        "    t.mat_pers_conj, " +
                        "    t.num_ass_conj, " +
                        "    t.dat_saisie, " +
                        "    t.obs, " +
                        "    t.obs_a, " +
                        "    t.envoi, " +
                        "    t.ann_plaf_imp, " +
                        "    t.reg_adh, " +
                        "    t.mod_pay, " +
                        "    t.dat_vir, " +
                        "    (select p.nom_pers || ' ' || p.pren_pers  from personnel p where p.mat_pers=t.mat_pers) AS nom_complet_pers, "
                        +
                        "    (select lib_assur  from assurance a where a.cod_assur = t.cod_assur)AS lib_assur, " +
                        "    (select f.nom_pren from famille f where f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers)AS nom_adherent "
                        +
                        "FROM BULT_SOIN t " +
                        "where cod_soc = :codSoc\n" +
                        "    and cod_assur = :codAssur\n" +
                        "    and NAT_BULT = 'C'\n" +
                        "    AND NVL(ENVOI, 'N') = 'N' \n" +
                        "    and cod_bord is null\n" +
                        "    and dat_saisie between nvl(:datDeb,dat_saisie) and nvl(:datFin,dat_saisie) " +
                        "order by dat_saisie DESC, mat_pers, num_fam", nativeQuery = true)
        List<BultSoinProjection> getBulletSoinConsultaion(@Param("codSoc") String codSoc,
                        @Param("codAssur") String codAssur, @Param("datDeb") LocalDate datDeb,
                        @Param("datFin") LocalDate datFin);

        @Query(value = "SELECT " +
                        "    t.cod_soc, " +
                        "    t.mat_pers, " +
                        "    t.num_fam, " +
                        "    t.dat_soin, " +
                        "    t.cod_bord, " +
                        "    t.cod_assur, " +
                        "    t.num_soin, " +
                        "    t.ord_bult, " +
                        "    t.tot_honor, " +
                        "    t.tot_net, " +
                        "    t.tot_remb, " +
                        "    t.reg_remb, " +
                        "    t.cod_malad, " +
                        "    t.num_pec, " +
                        "    t.cod_fil, " +
                        "    t.num_assur, " +
                        "    t.dat_prev_accouch, " +
                        "    t.nat_bult, " +
                        "    t.mat_pers_conj, " +
                        "    t.num_ass_conj, " +
                        "    t.dat_saisie, " +
                        "    t.obs, " +
                        "    t.obs_a, " +
                        "    t.envoi, " +
                        "    t.ann_plaf_imp, " +
                        "    t.reg_adh, " +
                        "    t.mod_pay, " +
                        "    t.dat_vir, " +
                        "    (select p.nom_pers || ' ' || p.pren_pers  from personnel p where p.mat_pers=t.mat_pers) AS nom_complet_pers, "
                        +
                        "    (select lib_assur  from assurance a where a.cod_assur = t.cod_assur)AS lib_assur, " +
                        "    (select f.nom_pren from famille f where f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers)AS nom_adherent "
                        +
                        "FROM BULT_SOIN t " +
                        "WHERE t.cod_soc = :codSoc AND t.cod_assur = :codAssur AND t.nat_bult = 'M' AND t.cod_bord = :codBord "
                        +
                        "ORDER BY t.dat_saisie DESC, t.mat_pers, t.num_fam", nativeQuery = true)
        List<BultSoinProjection> getBulletinSoinEnvoiMut(@Param("codSoc") String codSoc,
                        @Param("codBord") String codBord, @Param("codAssur") String codAssur);

        @Query(value = "SELECT " +
                        "    t.cod_soc, " +
                        "    t.mat_pers, " +
                        "    t.num_fam, " +
                        "    t.dat_soin, " +
                        "    t.cod_bord, " +
                        "    t.cod_assur, " +
                        "    t.num_soin, " +
                        "    t.ord_bult, " +
                        "    t.tot_honor, " +
                        "    t.tot_net, " +
                        "    t.tot_remb, " +
                        "    t.reg_remb, " +
                        "    t.cod_malad, " +
                        "    t.num_pec, " +
                        "    t.cod_fil, " +
                        "    t.num_assur, " +
                        "    t.dat_prev_accouch, " +
                        "    t.nat_bult, " +
                        "    t.mat_pers_conj, " +
                        "    t.num_ass_conj, " +
                        "    t.dat_saisie, " +
                        "    t.obs, " +
                        "    t.obs_a, " +
                        "    t.envoi, " +
                        "    t.ann_plaf_imp, " +
                        "    t.reg_adh, " +
                        "    t.mod_pay, " +
                        "    t.dat_vir, " +
                        "    (select p.nom_pers || ' ' || p.pren_pers  from personnel p where p.mat_pers=t.mat_pers) AS nom_complet_pers, "
                        +
                        "    (select lib_assur  from assurance a where a.cod_assur = t.cod_assur)AS lib_assur, " +
                        "    (select f.nom_pren from famille f where f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers)AS nom_adherent "
                        +
                        "FROM BULT_SOIN t " +
                        "where cod_soc = :codSoc\n" +
                        "    and cod_assur = :codAssur\n" +
                        "    and NAT_BULT = 'M'\n" +
                        "    and nvl(ENVOI,'N') = 'N'\n" +
                        "    and cod_bord is null\n" +
                        "    and dat_saisie between nvl(:datDeb,dat_saisie) and nvl(:datFin,dat_saisie) " +
                        "order by dat_saisie DESC, mat_pers, num_fam", nativeQuery = true)
        List<BultSoinProjection> getBulletSoinConsultaionMut(@Param("codSoc") String codSoc,
                        @Param("codAssur") String codAssur, @Param("datDeb") LocalDate datDeb,
                        @Param("datFin") LocalDate datFin);

        @Query(value = "SELECT t.mat_pers,\n" +
                        "       (SELECT p.nom_pers || ' ' || p.pren_pers\n" +
                        "          FROM personnel p\n" +
                        "         WHERE p.mat_pers = t.mat_pers\n" +
                        "           and p.cod_soc = t.cod_soc\n" +
                        "           AND ROWNUM = 1) AS nom_complet_pers,\n" +
                        "       t.cod_fil,\n" +
                        "       t.num_fam,\n" +
                        "       (SELECT f.NOM_PREN\n" +
                        "          FROM famille f\n" +
                        "         WHERE f.mat_pers = t.mat_pers\n" +
                        "           AND ROWNUM = 1) AS nom_pers,\n" +
                        "       t.dat_saisie,\n" +
                        "       t.dat_soin,\n" +
                        "       (SELECT NVL(pc.PLAFOND, 0) - NVL(pc.SOLD_PLAF, 0)\n" +
                        "          FROM plafond_cnam pc\n" +
                        "         WHERE pc.MAT_PERS = t.MAT_PERS\n" +
                        "           AND pc.cod_soc = :cod_soc\n" +
                        "           AND pc.ANNEE_CNAM = TO_NUMBER(TO_CHAR(t.dat_soin, 'YYYY'))) AS solde,\n" +
                        "       t.tot_honor,\n" +
                        "       t.tot_net\n" +
                        "  FROM bult_soin t, bord_envoi e, personnel p\n" +
                        " WHERE t.cod_soc = :cod_soc\n" +
                        "   AND t.nat_bult = 'C'\n" +
                        "   AND t.cod_assur = e.cod_assur\n" +
                        "   AND t.cod_bord = e.cod_bord\n" +
                        "   AND p.cod_soc = t.cod_soc\n" +
                        "   AND p.mat_pers = t.mat_pers\n" +
                        "   AND e.cod_bord = :cod_bord\n" +
                        "   and (t.cod_soc, t.mat_pers, t.num_fam, t.dat_soin) not in\n" +
                        "       (select cod_soc, mat_pers, num_fam, dat_soin\n" +
                        "          from bult_arriver\n" +
                        "         where cod_soc = :cod_soc\n" +
                        "           and cod_bord = e.cod_bord)\n" +
                        " ORDER BY t.dat_saisie DESC, t.dat_soin DESC\n", nativeQuery = true)
        List<ControleBordCnamProjection> getControleBordCnam(@Param("cod_soc") String cod_soc,
                        @Param("cod_bord") String cod_bord);

        @Query(value = "select r.LIB_ACT,COD_ACT,let_cod,COT_ACT,ACT_PRIX,r.abrv_act,r.TAUX_ACT\n" +
                        "from ref_act r ,acte a,bareme_remb b \n" +
                        "where r.ABRV_ACT = a.abrv_act\n" +
                        "and nvl(a.parente,:parente) = :parente\n" +
                        "and nvl(a.sexe,:sexe) = :sexe\n" +
                        "and b.abrv_act = a.abrv_act\n" +
                        "and b.cod_fil = :codFil\n" +
                        "and b.COD_ASSUR = :codAssur", nativeQuery = true)
        List<ActProjection> getActes(@Param("parente") String parente, @Param("sexe") String sexe,
                        @Param("codFil") String codFil, @Param("codAssur") String codAssur);

        @Query(value = "select COD_ACT,r.LIB_ACT,let_cod,COT_ACT,ACT_PRIX,r.abrv_act,r.TAUX_ACT\n" +
                        "from ref_act r ,acte a,bareme_remb b \n" +
                        "where r.ABRV_ACT = a.abrv_act\n" +
                        "and b.abrv_act = a.abrv_act\n" +
                        "and b.cod_fil = :codFil\n" +
                        "and b.COD_ASSUR = :codAssur", nativeQuery = true)
        List<ActProjection> getActesManuel(@Param("codFil") String codFil, @Param("codAssur") String codAssur);

        @Query(value = "select r.lib_med,cod_med,mdc_prix,med_prix,prix_remb,r.abrv_act\n" +
                        "from ref_med r ,acte a,bareme_remb b ,ref_fill_act t\n" +
                        "where r.ABRV_ACT = a.abrv_act\n" +
                        "and b.abrv_act = a.abrv_act\n" +
                        "and b.cod_fil = :codFil \n" +
                        "and b.COD_ASSUR = :codAssur \n" +
                        "and b.abrv_act = t.abrv_act\n" +
                        "and t.cod_fil = :codFil", nativeQuery = true)
        List<MedProjection> getListMedCnam(@Param("codFil") String codFil, @Param("codAssur") String codAssur);

        @Query(value = "SELECT t.cod_soc, t.mat_pers, t.num_fam, t.dat_soin, t.cod_bord, t.cod_assur, " +
                        "t.num_soin, t.ord_bult, t.tot_honor, t.tot_net, t.tot_remb, t.reg_remb, t.cod_malad, " +
                        "t.num_pec, t.cod_fil, t.num_assur, t.dat_prev_accouch, t.nat_bult, t.mat_pers_conj, " +
                        "t.num_ass_conj, t.dat_saisie, t.obs, t.obs_a, t.envoi, t.ann_plaf_imp, t.reg_adh, " +
                        "t.mod_pay, t.dat_vir, p.mat_int, p.dat_nais, " +
                        "p.nom_pers || ' ' || p.pren_pers AS nomCompletPers, " +
                        "a.lib_assur AS libAssur, r.lib_fill AS libFill, f.nom_pren AS nomAdherent " +
                        "FROM bult_soin t " +
                        "LEFT JOIN personnel p ON p.mat_pers = t.mat_pers " +
                        "LEFT JOIN assurance a ON a.cod_assur = t.cod_assur " +
                        "LEFT JOIN ref_filliere r ON r.cod_fil = t.cod_fil " +
                        "LEFT JOIN famille f ON f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers " +
                        "WHERE t.cod_soc = :soc AND t.NAT_BULT = 'C' " +
                        "ORDER BY t.dat_saisie DESC, t.dat_soin DESC", nativeQuery = true)
        List<BultSoinProjection> getBultCnam(@Param("soc") String soc);
        @Query(value = "SELECT t.cod_soc, t.mat_pers, t.num_fam, t.dat_soin, t.cod_bord, t.cod_assur, " +
                "t.num_soin, t.ord_bult, t.tot_honor, t.tot_net, t.tot_remb, t.reg_remb, t.cod_malad, " +
                "t.num_pec, t.cod_fil, t.num_assur, t.dat_prev_accouch, t.nat_bult, t.mat_pers_conj, " +
                "t.num_ass_conj, t.dat_saisie, t.obs, t.obs_a, t.envoi, t.ann_plaf_imp, t.reg_adh, " +
                "t.mod_pay, t.dat_vir, p.mat_int, p.dat_nais, " +
                "p.nom_pers || ' ' || p.pren_pers AS nomCompletPers, " +
                "a.lib_assur AS libAssur, r.lib_fill AS libFill, f.nom_pren AS nomAdherent " +
                "FROM bult_soin t " +
                "LEFT JOIN personnel p ON p.mat_pers = t.mat_pers " +
                "LEFT JOIN assurance a ON a.cod_assur = t.cod_assur " +
                "LEFT JOIN ref_filliere r ON r.cod_fil = t.cod_fil " +
                "LEFT JOIN famille f ON f.num_fam = t.num_fam AND f.mat_pers = t.mat_pers " +
                "WHERE t.cod_soc = :soc AND t.NAT_BULT = 'M' " +
                "ORDER BY t.dat_saisie DESC, t.dat_soin DESC", nativeQuery = true)
        List<BultSoinProjection> getBultMut(@Param("soc") String soc);
        @Query(value = "select * from bord_envoi where cod_soc = :soc and TYP_BORD = 'C'\n" +
                        "and nvl(envoi_bord,'O') = 'O' and cod_bord is not null order by dat_bord desc", nativeQuery = true)
        List<BordEnvoiPrejection> getBordtCnam(@Param("soc") String soc);

        @Query(value = "select * from bult_soin where cod_soc = :soc and NAT_BULT = 'C'\n" +
                        "and nvl(envoi,'N') = 'O' and cod_bord=:codBord ORDER BY dat_saisie DESC, dat_soin DESC", nativeQuery = true)
        List<BultSoin> getBultCnamRecep(@Param("soc") String soc, @Param("codBord") String codBord);

        @Query(value = "SELECT t.mat_pers,\n" +
                        "                   (SELECT p.nom_pers || ' ' || p.pren_pers\n" +
                        "                      FROM personnel p\n" +
                        "                     WHERE p.mat_pers = t.mat_pers\n" +
                        "                       and p.cod_soc = t.cod_soc\n" +
                        "                       AND ROWNUM = 1) AS nom_complet_pers,\n" +
                        "                   t.cod_fil,\n" +
                        "                   t.num_fam,\n" +
                        "                   (SELECT f.NOM_PREN\n" +
                        "                      FROM famille f\n" +
                        "                     WHERE f.mat_pers = t.mat_pers\n" +
                        "                       AND ROWNUM = 1) AS nom_pers,\n" +
                        "                   t.dat_saisie,\n" +
                        "                   t.dat_soin,\n" +
                        "                   (SELECT NVL(pc.PLAFOND, 0) - NVL(pc.SOLD_PLAF, 0)\n" +
                        "                      FROM plafond_cnam pc\n" +
                        "                     WHERE pc.MAT_PERS = t.MAT_PERS\n" +
                        "                       AND pc.cod_soc =:cod_soc\n" +
                        "                       AND pc.ANNEE_CNAM = TO_NUMBER(TO_CHAR(t.dat_soin, 'YYYY'))) AS solde,\n"
                        +
                        "                   t.tot_honor,\n" +
                        "                   t.tot_net\n" +
                        "              FROM bult_soin t, bord_envoi e, personnel p\n" +
                        "             WHERE t.cod_soc =:cod_soc\n" +
                        "                AND t.cod_assur = e.cod_assur\n" +
                        "               AND t.cod_bord = e.cod_bord\n" +
                        "               AND p.cod_soc = t.cod_soc\n" +
                        "               AND p.mat_pers = t.mat_pers\n" +
                        "               AND e.cod_bord =:cod_bord", nativeQuery = true)
        List<ControleBordCnamProjection> getControleBordMutuelle(@Param("cod_soc") String cod_soc,
                        @Param("cod_bord") String cod_bord);

        @Query(value = "SELECT t.mat_pers,\n" +
                        "                   (SELECT p.nom_pers || ' ' || p.pren_pers\n" +
                        "                      FROM personnel p\n" +
                        "                     WHERE p.mat_pers = t.mat_pers\n" +
                        "                       and p.cod_soc = t.cod_soc\n" +
                        "                       AND ROWNUM = 1) AS nom_complet_pers,\n" +
                        "                   t.cod_fil,\n" +
                        "                   t.num_fam,\n" +
                        "                   (SELECT f.NOM_PREN\n" +
                        "                      FROM famille f\n" +
                        "                     WHERE f.mat_pers = t.mat_pers\n" +
                        "                       AND ROWNUM = 1) AS nom_pers,\n" +
                        "                   t.dat_saisie,\n" +
                        "                   t.dat_soin,\n" +
                        "                   (SELECT NVL(pc.PLAFOND, 0) - NVL(pc.SOLD_PLAF, 0)\n" +
                        "                      FROM plafond_cnam pc\n" +
                        "                     WHERE pc.MAT_PERS = t.MAT_PERS\n" +
                        "                       AND pc.cod_soc =:cod_soc\n" +
                        "                       AND pc.ANNEE_CNAM = TO_NUMBER(TO_CHAR(t.dat_soin, 'YYYY'))) AS solde,\n"
                        +
                        "                   t.tot_honor,\n" +
                        "                   t.tot_net,t.reg_adh\n" +
                        "              FROM bult_soin t, bord_envoi e, personnel p\n" +
                        "             WHERE t.cod_soc =:cod_soc\n" +
                        "                AND t.cod_assur = e.cod_assur\n" +
                        "               AND t.cod_bord = e.cod_bord\n" +
                        "               AND p.cod_soc = t.cod_soc\n" +
                        "               AND p.mat_pers = t.mat_pers\n" +
                        "               AND e.cod_bord =:cod_bord and t.mod_pay='E'", nativeQuery = true)
        List<ControleBordCnamProjection> getPayBordMutuelle(@Param("cod_soc") String cod_soc,
                        @Param("cod_bord") String cod_bord);

        @Query(value = "SELECT t.mat_pers,\n" +
                        "                   (SELECT p.nom_pers || ' ' || p.pren_pers\n" +
                        "                      FROM personnel p\n" +
                        "                     WHERE p.mat_pers = t.mat_pers\n" +
                        "                       and p.cod_soc = t.cod_soc\n" +
                        "                       AND ROWNUM = 1) AS nom_complet_pers,\n" +
                        "                   t.cod_fil,\n" +
                        "                   t.num_fam,\n" +
                        "                   (SELECT f.NOM_PREN\n" +
                        "                      FROM famille f\n" +
                        "                     WHERE f.mat_pers = t.mat_pers\n" +
                        "                       AND ROWNUM = 1) AS nom_pers,\n" +
                        "                   t.dat_saisie,\n" +
                        "                   t.dat_soin,\n" +
                        "                   (SELECT NVL(pc.PLAFOND, 0) - NVL(pc.SOLD_PLAF, 0)\n" +
                        "                      FROM plafond_cnam pc\n" +
                        "                     WHERE pc.MAT_PERS = t.MAT_PERS\n" +
                        "                       AND pc.cod_soc =:cod_soc\n" +
                        "                       AND pc.ANNEE_CNAM = TO_NUMBER(TO_CHAR(t.dat_soin, 'YYYY'))) AS solde,\n"
                        +
                        "                   t.tot_honor,\n" +
                        "                   t.tot_net,t.reg_adh,t.mod_pay,t.dat_vir,t.tot_remb\n" +
                        "              FROM bult_soin t, bord_envoi e, personnel p\n" +
                        "             WHERE t.cod_soc =:cod_soc\n" +
                        "                AND t.cod_assur = e.cod_assur\n" +
                        "               AND t.cod_bord = e.cod_bord\n" +
                        "               AND p.cod_soc = t.cod_soc\n" +
                        "               AND p.mat_pers = t.mat_pers\n" +
                        "               AND e.cod_bord =:cod_bord", nativeQuery = true)
        List<ControleBordCnamProjection> MajModePayement(@Param("cod_soc") String cod_soc,
                        @Param("cod_bord") String cod_bord);

        @Query(value = "SELECT t.mat_pers,\n" +
                        "                   (SELECT p.nom_pers || ' ' || p.pren_pers\n" +
                        "                      FROM personnel p\n" +
                        "                     WHERE p.mat_pers = t.mat_pers\n" +
                        "                       and p.cod_soc = t.cod_soc\n" +
                        "                       AND ROWNUM = 1) AS nom_complet_pers,\n" +
                        "                   t.cod_fil,\n" +
                        "                   t.num_fam,\n" +
                        "                   (SELECT f.NOM_PREN\n" +
                        "                      FROM famille f\n" +
                        "                     WHERE f.mat_pers = t.mat_pers\n" +
                        "                       AND ROWNUM = 1) AS nom_pers,\n" +
                        "                   t.dat_saisie,\n" +
                        "                   t.dat_soin,\n" +
                        "                   (SELECT NVL(pc.PLAFOND, 0) - NVL(pc.SOLD_PLAF, 0)\n" +
                        "                      FROM plafond_cnam pc\n" +
                        "                     WHERE pc.MAT_PERS = t.MAT_PERS\n" +
                        "                       AND pc.cod_soc =:cod_soc\n" +
                        "                       AND pc.ANNEE_CNAM = TO_NUMBER(TO_CHAR(t.dat_soin, 'YYYY'))) AS solde,\n"
                        +
                        "                   t.tot_honor,\n" +
                        "                   t.tot_net,t.reg_adh,t.mod_pay,t.dat_vir\n" +
                        "              FROM bult_soin t, bord_envoi e, personnel p\n" +
                        "             WHERE t.cod_soc =:cod_soc\n" +
                        "                AND t.cod_assur = e.cod_assur\n" +
                        "               AND t.cod_bord = e.cod_bord\n" +
                        "               AND p.cod_soc = t.cod_soc\n" +
                        "               AND p.mat_pers = t.mat_pers\n" +
                        "               AND e.cod_bord =:cod_bord and mod_pay='V'  and nvl(reg_adh,'N')='N' order by t.dat_saisie DESC, t.dat_soin DESC, t.cod_soc, lpad(t.mat_pers,10,'0'), t.num_fam", nativeQuery = true)
        List<ControleBordCnamProjection> getBultSoinCpt(@Param("cod_soc") String cod_soc,
                        @Param("cod_bord") String cod_bord);

        @Query(value = "SELECT t.mat_pers,\n" +
                        "                   (SELECT p.nom_pers || ' ' || p.pren_pers\n" +
                        "                      FROM personnel p\n" +
                        "                     WHERE p.mat_pers = t.mat_pers\n" +
                        "                       and p.cod_soc = t.cod_soc\n" +
                        "                       AND ROWNUM = 1) AS nom_complet_pers,\n" +
                        "                   t.cod_fil,\n" +
                        "                   t.num_fam,\n" +
                        "                   (SELECT f.NOM_PREN\n" +
                        "                      FROM famille f\n" +
                        "                     WHERE f.mat_pers = t.mat_pers\n" +
                        "                       AND ROWNUM = 1) AS nom_pers,\n" +
                        "                   t.dat_saisie,\n" +
                        "                   t.dat_soin,\n" +
                        "                   (SELECT NVL(pc.PLAFOND, 0) - NVL(pc.SOLD_PLAF, 0)\n" +
                        "                      FROM plafond_cnam pc\n" +
                        "                     WHERE pc.MAT_PERS = t.MAT_PERS\n" +
                        "                       AND pc.cod_soc =:cod_soc\n" +
                        "                       AND pc.ANNEE_CNAM = TO_NUMBER(TO_CHAR(t.dat_soin, 'YYYY'))) AS solde,\n"
                        +
                        "                   t.tot_honor,\n" +
                        "                   t.tot_net,t.reg_adh,t.mod_pay,t.dat_vir\n" +
                        "              FROM bult_soin t, bord_envoi e, personnel p\n" +
                        "             WHERE t.cod_soc =:cod_soc\n" +
                        "                AND t.cod_assur = e.cod_assur\n" +
                        "               AND t.cod_bord = e.cod_bord\n" +
                        "               AND p.cod_soc = t.cod_soc\n" +
                        "               AND p.mat_pers = t.mat_pers\n" +
                        "               AND e.cod_bord =:cod_bord and mod_pay='C'  and nvl(reg_adh,'N')='N' order by t.dat_saisie DESC, t.dat_soin DESC, t.cod_soc, lpad(t.mat_pers,10,'0'), t.num_fam", nativeQuery = true)
        List<ControleBordCnamProjection> getBultSoinCheqCpt(@Param("cod_soc") String cod_soc,
                        @Param("cod_bord") String cod_bord);

        @Query(value = "select * from bult_soin where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:dat", nativeQuery = true)
        BultSoin getBultSoinById(@Param("soc") String soc, @Param("mat") String mat, @Param("numFam") String numFam,
                        @Param("dat") LocalDate dat);
}
