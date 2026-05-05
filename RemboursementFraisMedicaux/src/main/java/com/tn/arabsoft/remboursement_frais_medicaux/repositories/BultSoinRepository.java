package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.BultSoin;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BultSoinCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings({ "java:S117" })
public interface BultSoinRepository extends JpaRepository<BultSoin, BultSoinCle> {

       @Query(value = "select count(*) from bult_soin where cod_assur = :codAssur and num_soin = :numSoin", nativeQuery = true)
       Long getNbreBult(@Param("codAssur") String codAssur, @Param("numSoin") String numSoin);

       @Query(value = """
                     select
                         p.num_pec,
                         p.dat_pec,
                         concat(r.etab_rsoc, concat(' ', r.pr_rsoc)) as etablis
                     from prise_charge p
                     join ref_etablis r
                       on p.prf_typ = r.prf_typ
                      and p.prf_cod = r.prf_cod
                     where p.cod_soc = :soc
                       and p.mat_pers = :mat
                       and p.num_fam = :numFam
                       and p.dat_eff <= :datSoin
                       and p.etat_pec = 'V'
                     """, nativeQuery = true)
       List<PECProjection> getListEtablisPec(
                     @Param("soc") String soc,
                     @Param("mat") String mat,
                     @Param("numFam") String numFam,
                     @Param("datSoin") LocalDate datSoin);

       @Query(value = """
                     select
                         b.mat_pers as matricule,
                         b.num_assur,
                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nom,
                         b.num_fam as prestataire,
                         b.dat_soin
                     from bult_soin b
                     join personnel p
                       on p.cod_soc = b.cod_soc
                      and p.mat_pers = b.mat_pers
                     where b.cod_soc = :soc
                       and b.nat_bult = 'M'
                       and b.cod_bord is null
                       and coalesce(b.envoi, 'N') = 'N'
                     order by b.dat_soin desc, b.mat_pers, b.num_fam
                     """, nativeQuery = true)
       List<PersConsProjection> getPersCons(@Param("soc") String soc);

       @Query(value = """
                     select
                         t.cod_soc,
                         t.mat_pers,
                         t.num_fam,
                         t.dat_soin,
                         t.cod_bord,
                         t.cod_assur,
                         t.num_soin,
                         t.ord_bult,
                         t.tot_honor,
                         t.tot_net,
                         t.tot_remb,
                         t.reg_remb,
                         t.cod_malad,
                         t.num_pec,
                         t.cod_fil,
                         t.num_assur,
                         t.dat_prev_accouch,
                         t.nat_bult,
                         t.mat_pers_conj,
                         t.num_ass_conj,
                         t.dat_saisie,
                         t.obs,
                         t.obs_a,
                         t.envoi,
                         t.ann_plaf_imp,
                         t.reg_adh,
                         t.mod_pay,
                         t.dat_vir,
                         p.mat_int,
                         p.dat_nais,
                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nomCompletPers,
                         a.lib_assur as libAssur,
                         r.lib_fill as libFill,
                         f.nom_pren as nomAdherent
                     from bult_soin t
                     left join personnel p
                            on p.mat_pers = t.mat_pers
                     left join assurance a
                            on a.cod_assur = t.cod_assur
                     left join ref_filliere r
                            on r.cod_fil = t.cod_fil
                     left join famille f
                            on f.num_fam = t.num_fam
                           and f.mat_pers = t.mat_pers
                     where t.nat_bult = 'M'
                       and coalesce(t.envoi, 'N') = 'N'
                     order by t.dat_saisie desc, t.dat_soin desc, t.mat_pers, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletinSoin();

       @Query(value = """
                     select
                         t.cod_soc,
                         t.mat_pers,
                         t.num_fam,
                         t.dat_soin,
                         t.cod_bord,
                         t.cod_assur,
                         t.num_soin,
                         t.ord_bult,
                         t.tot_honor,
                         t.tot_net,
                         t.tot_remb,
                         t.reg_remb,
                         t.cod_malad,
                         t.num_pec,
                         t.cod_fil,
                         t.num_assur,
                         t.dat_prev_accouch,
                         t.nat_bult,
                         t.mat_pers_conj,
                         t.num_ass_conj,
                         t.dat_saisie,
                         t.obs,
                         t.obs_a,
                         t.envoi,
                         t.ann_plaf_imp,
                         t.reg_adh,
                         t.mod_pay,
                         t.dat_vir,
                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_complet_pers,
                         a.lib_assur as lib_assur,
                         r.lib_fill as lib_fill,
                         f.nom_pren as nom_adherent
                     from bult_soin t
                     left join personnel p
                            on p.mat_pers = t.mat_pers
                     left join assurance a
                            on a.cod_assur = t.cod_assur
                     left join ref_filliere r
                            on r.cod_fil = t.cod_fil
                     left join famille f
                            on f.num_fam = t.num_fam
                           and f.mat_pers = t.mat_pers
                     where t.nat_bult = 'M'
                       and coalesce(t.envoi, 'N') = 'O'
                       and (:mat is null or t.mat_pers = :mat)
                       and (:num_fam is null or t.num_fam = :num_fam)
                       and (:dat_soin is null or t.dat_soin = :dat_soin)
                     order by t.dat_saisie desc, t.dat_soin desc, t.mat_pers, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletinSoinCons(
                     @Param("mat") String mat,
                     @Param("num_fam") String num_fam,
                     @Param("dat_soin") LocalDate dat_soin);

       @Query(value = """
                     select
                         t.cod_soc,
                         t.mat_pers,
                         t.num_fam,
                         t.dat_soin,
                         t.cod_bord,
                         t.cod_assur,
                         t.num_soin,
                         t.ord_bult,
                         t.tot_honor,
                         t.tot_net,
                         t.tot_remb,
                         t.reg_remb,
                         t.cod_malad,
                         t.num_pec,
                         t.cod_fil,
                         t.num_assur,
                         t.dat_prev_accouch,
                         t.nat_bult,
                         t.mat_pers_conj,
                         t.num_ass_conj,
                         t.dat_saisie,
                         t.obs,
                         t.obs_a,
                         t.envoi,
                         t.ann_plaf_imp,
                         t.reg_adh,
                         t.mod_pay,
                         t.dat_vir,
                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_complet_pers,
                         a.lib_assur as lib_assur,
                         r.lib_fill as lib_fill,
                         f.nom_pren as nom_adherent
                     from bult_soin t
                     left join personnel p
                            on p.mat_pers = t.mat_pers
                     left join assurance a
                            on a.cod_assur = t.cod_assur
                     left join ref_filliere r
                            on r.cod_fil = t.cod_fil
                     left join famille f
                            on f.num_fam = t.num_fam
                           and f.mat_pers = t.mat_pers
                     where t.nat_bult = 'C'
                       and coalesce(t.envoi, 'N') = 'O'
                       and (:mat is null or t.mat_pers = :mat)
                       and (:num_fam is null or t.num_fam = :num_fam)
                       and (:dat_soin is null or t.dat_soin = :dat_soin)
                     order by t.dat_saisie desc, t.dat_soin desc, t.mat_pers, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletinCnamCons(
                     @Param("mat") String mat,
                     @Param("num_fam") String num_fam,
                     @Param("dat_soin") LocalDate dat_soin);

       @Query(value = """
                     select
                         b.abrv_act,
                         a.lib_act
                     from bareme_remb b
                     join acte a
                       on b.abrv_act = a.abrv_act
                     where b.cod_fil = :codFil
                       and b.cod_assur = :codAssur
                       and coalesce(a.parente, :parente) = :parente
                       and coalesce(a.sexe, :sexe) = :sexe
                       and not exists (
                             select 1
                             from ref_fill_act r
                             where r.cod_fil = :codFil
                               and r.abrv_act = a.abrv_act
                       )
                     """, nativeQuery = true)
       List<ActeProjection> getActe(
                     @Param("codFil") String codFil,
                     @Param("codAssur") String codAssur,
                     @Param("parente") String parente,
                     @Param("sexe") String sexe);

       @Query(value = """
                     select
                         t.cod_soc,
                         t.mat_pers,
                         t.num_fam,
                         t.dat_soin,
                         t.cod_bord,
                         t.cod_assur,
                         t.num_soin,
                         t.ord_bult,
                         t.tot_honor,
                         t.tot_net,
                         t.tot_remb,
                         t.reg_remb,
                         t.cod_malad,
                         t.num_pec,
                         t.cod_fil,
                         t.num_assur,
                         t.dat_prev_accouch,
                         t.nat_bult,
                         t.mat_pers_conj,
                         t.num_ass_conj,
                         t.dat_saisie,
                         t.obs,
                         t.obs_a,
                         t.envoi,
                         t.ann_plaf_imp,
                         t.reg_adh,
                         t.mod_pay,
                         t.dat_vir,

                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_complet_pers,

                         max(p.num_retr) over (partition by p.mat_pers) as num_retr,

                         a.lib_assur as lib_assur,
                         r.lib_fill as lib_fill,
                         rr.lib_remb as lib_remb,

                         case
                             when t.num_fam = 0 then 'Adhérent'
                             else f.nom_pren
                         end as nom_adherent

                     from bult_soin t

                     left join personnel p
                            on p.mat_pers = t.mat_pers
                           and p.cod_soc = t.cod_soc

                     left join assurance a
                            on a.cod_assur = t.cod_assur

                     left join ref_filliere r
                            on r.cod_fil = t.cod_fil

                     left join regime_remb rr
                            on rr.reg_remb = t.reg_remb

                     left join famille f
                            on f.num_fam = t.num_fam
                           and f.mat_pers = t.mat_pers
                           and f.cod_soc = t.cod_soc

                     where t.cod_soc = :codSoc
                       and t.cod_assur = :codAssur
                       and t.nat_bult = 'C'
                       and t.cod_bord = :codBord

                     order by t.dat_saisie desc, t.mat_pers, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletinSoinEnvoi(
                     @Param("codSoc") String codSoc,
                     @Param("codBord") String codBord,
                     @Param("codAssur") String codAssur);

       @Query(value = """
                     select
                         t.cod_soc,
                         t.mat_pers,
                         t.num_fam,
                         t.dat_soin,
                         t.cod_bord,
                         t.cod_assur,
                         t.num_soin,
                         t.ord_bult,
                         t.tot_honor,
                         t.tot_net,
                         t.tot_remb,
                         t.reg_remb,
                         t.cod_malad,
                         t.num_pec,
                         t.cod_fil,
                         t.num_assur,
                         t.dat_prev_accouch,
                         t.nat_bult,
                         t.mat_pers_conj,
                         t.num_ass_conj,
                         t.dat_saisie,
                         t.obs,
                         t.obs_a,
                         t.envoi,
                         t.ann_plaf_imp,
                         t.reg_adh,
                         t.mod_pay,
                         t.dat_vir,

                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_complet_pers,

                         a.lib_assur as lib_assur,

                         f.nom_pren as nom_adherent

                     from bult_soin t

                     left join personnel p
                            on p.mat_pers = t.mat_pers
                           and p.cod_soc = t.cod_soc

                     left join assurance a
                            on a.cod_assur = t.cod_assur

                     left join famille f
                            on f.num_fam = t.num_fam
                           and f.mat_pers = t.mat_pers
                           and f.cod_soc = t.cod_soc

                     where t.cod_soc = :codSoc
                       and t.cod_assur = :codAssur
                       and t.nat_bult = 'C'
                       and coalesce(t.envoi, 'N') = 'N'
                       and t.cod_bord is null
                       and t.dat_saisie between coalesce(:datDeb, t.dat_saisie)
                                           and coalesce(:datFin, t.dat_saisie)

                     order by t.dat_saisie desc, t.mat_pers, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletSoinConsultaion(
                     @Param("codSoc") String codSoc,
                     @Param("codAssur") String codAssur,
                     @Param("datDeb") LocalDate datDeb,
                     @Param("datFin") LocalDate datFin);

       @Query(value = """
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.cod_bord,
                            t.cod_assur,
                            t.num_soin,
                            t.ord_bult,
                            t.tot_honor,
                            t.tot_net,
                            t.tot_remb,
                            t.reg_remb,
                            t.cod_malad,
                            t.num_pec,
                            t.cod_fil,
                            t.num_assur,
                            t.dat_prev_accouch,
                            t.nat_bult,
                            t.mat_pers_conj,
                            t.num_ass_conj,
                            t.dat_saisie,
                            t.obs,
                            t.obs_a,
                            t.envoi,
                            t.ann_plaf_imp,
                            t.reg_adh,
                            t.mod_pay,
                            t.dat_vir,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_complet_pers,
                            a.lib_assur as lib_assur,
                            f.nom_pren as nom_adherent
                     from bult_soin t
                     left join personnel p
                            on p.mat_pers = t.mat_pers
                           and p.cod_soc = t.cod_soc
                     left join assurance a
                            on a.cod_assur = t.cod_assur
                     left join famille f
                            on f.num_fam = t.num_fam
                           and f.mat_pers = t.mat_pers
                           and f.cod_soc = t.cod_soc
                     where t.cod_soc = :codSoc
                       and t.cod_assur = :codAssur
                       and t.cod_bord = :codBord
                       and t.nat_bult = 'M'
                     order by t.dat_saisie desc,
                              t.mat_pers,
                              t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletinSoinEnvoiMut(
                     @Param("codSoc") String codSoc,
                     @Param("codBord") String codBord,
                     @Param("codAssur") String codAssur);

       @Query(value = """
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.cod_bord,
                            t.cod_assur,
                            t.num_soin,
                            t.ord_bult,
                            t.tot_honor,
                            t.tot_net,
                            t.tot_remb,
                            t.reg_remb,
                            t.cod_malad,
                            t.num_pec,
                            t.cod_fil,
                            t.num_assur,
                            t.dat_prev_accouch,
                            t.nat_bult,
                            t.mat_pers_conj,
                            t.num_ass_conj,
                            t.dat_saisie,
                            t.obs,
                            t.obs_a,
                            t.envoi,
                            t.ann_plaf_imp,
                            t.reg_adh,
                            t.mod_pay,
                            t.dat_vir,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            a.lib_assur as lib_assur,
                            f.nom_pren as nom_adherent
                     from bult_soin t
                     left join personnel p
                            on p.mat_pers = t.mat_pers
                           and p.cod_soc = t.cod_soc
                     left join assurance a
                            on a.cod_assur = t.cod_assur
                     left join famille f
                            on f.num_fam = t.num_fam
                           and f.mat_pers = t.mat_pers
                           and f.cod_soc = t.cod_soc
                     where t.cod_soc = :codSoc
                       and t.cod_assur = :codAssur
                       and t.nat_bult = 'M'
                       and coalesce(t.envoi, 'N') = 'N'
                       and t.cod_bord is null
                       and t.dat_saisie between coalesce(:datDeb, t.dat_saisie)
                                           and coalesce(:datFin, t.dat_saisie)
                     order by t.dat_saisie desc,
                              t.mat_pers,
                              t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletSoinConsultaionMut(
                     @Param("codSoc") String codSoc,
                     @Param("codAssur") String codAssur,
                     @Param("datDeb") LocalDate datDeb,
                     @Param("datFin") LocalDate datFin);

       @Query(value = """
                     select t.mat_pers,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            t.cod_fil,
                            t.num_fam,
                            f.nom_pren as nom_pers,
                            t.dat_saisie,
                            t.dat_soin,
                            (pc.plafond - pc.sold_plaf) as solde,
                            t.tot_honor,
                            t.tot_net
                     from bult_soin t
                     join bord_envoi e
                          on t.cod_soc = e.cod_soc
                         and t.cod_bord  = e.cod_bord
                     join personnel p
                          on p.cod_soc = t.cod_soc
                         and p.mat_pers = t.mat_pers
                     left join famille f
                          on f.cod_soc = t.cod_soc
                         and f.mat_pers = t.mat_pers
                         and f.num_fam = t.num_fam
                     left join plafond_cnam pc
                          on pc.cod_soc = t.cod_soc
                         and pc.mat_pers = t.mat_pers
                         and pc.annee_cnam = extract(year from t.dat_soin)
                     where t.cod_soc = :cod_soc
                       and t.nat_bult = 'C'
                       and e.cod_bord = :cod_bord
                       and not exists (
                             select 1
                             from bult_arriver ba
                             where ba.cod_soc = t.cod_soc
                               and ba.cod_bord = e.cod_bord
                               and ba.mat_pers = t.mat_pers
                               and ba.num_fam  = t.num_fam
                               and ba.dat_soin = t.dat_soin
                       )
                     order by t.dat_saisie desc,
                              t.dat_soin desc
                     """, nativeQuery = true)
       List<ControleBordCnamProjection> getControleBordCnam(
                     @Param("cod_soc") String cod_soc,
                     @Param("cod_bord") String cod_bord);

       @Query(value = """
                     select t.mat_pers,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            t.cod_fil,
                            t.num_fam,
                            case
                                when t.num_fam = 0 then 'Adhérent'
                                else f.nom_pren
                            end as nom_pers,
                            t.dat_saisie,
                            t.dat_soin,
                            coalesce(pc.plafond, 0) - coalesce(pc.sold_plaf, 0) as solde,
                            t.tot_honor,
                            t.tot_net
                       from bult_soin t
                       join bord_envoi e
                         on t.cod_soc = e.cod_soc
                        and t.cod_bord = e.cod_bord
                       join personnel p
                         on p.cod_soc = t.cod_soc
                        and p.mat_pers = t.mat_pers
                       left join famille f
                         on f.cod_soc = t.cod_soc
                        and f.mat_pers = t.mat_pers
                        and f.num_fam = t.num_fam
                       left join plafond_cnam pc
                         on pc.cod_soc = t.cod_soc
                        and pc.mat_pers = t.mat_pers
                        and pc.annee_cnam = extract(year from t.dat_soin)
                      where t.cod_soc = :cod_soc
                        and e.cod_bord = :cod_bord
                        and t.nat_bult = 'M'
                     """, nativeQuery = true)
       List<ControleBordCnamProjection> getControleBordMutuelle(
                     @Param("cod_soc") String cod_soc,
                     @Param("cod_bord") String cod_bord);

       @Query(value = """
                     select r.lib_act,
                            r.cod_act,
                            r.let_cod,
                            r.cot_act,
                            r.act_prix,
                            r.abrv_act,
                            r.taux_act
                     from ref_act r
                     join acte a
                          on r.abrv_act = a.abrv_act
                     join bareme_remb b
                          on b.abrv_act = a.abrv_act
                     where coalesce(a.parente, :parente) = :parente
                       and coalesce(a.sexe, :sexe) = :sexe
                       and b.cod_fil = :codFil
                       and b.cod_assur = :codAssur
                     """, nativeQuery = true)
       List<ActProjection> getActes(
                     @Param("parente") String parente,
                     @Param("sexe") String sexe,
                     @Param("codFil") String codFil,
                     @Param("codAssur") String codAssur);

       @Query(value = """
                     select r.cod_act,
                            r.lib_act,
                            r.let_cod,
                            r.cot_act,
                            r.act_prix,
                            r.abrv_act,
                            r.taux_act
                     from ref_act r
                     join acte a
                          on r.abrv_act = a.abrv_act
                     join bareme_remb b
                          on b.abrv_act = a.abrv_act
                     where b.cod_fil = :codFil
                       and b.cod_assur = :codAssur
                     """, nativeQuery = true)
       List<ActProjection> getActesManuel(
                     @Param("codFil") String codFil,
                     @Param("codAssur") String codAssur);

       @Query(value = """
                     select r.lib_med,
                            r.cod_med,
                            r.mdc_prix,
                            r.med_prix,
                            r.prix_remb,
                            r.abrv_act
                     from ref_med r
                     join acte a
                          on r.abrv_act = a.abrv_act
                     join bareme_remb b
                          on b.abrv_act = a.abrv_act
                     join ref_fill_act t
                          on t.abrv_act = a.abrv_act
                     where b.cod_fil = :codFil
                       and b.cod_assur = :codAssur
                       and t.cod_fil = :codFil
                     """, nativeQuery = true)
       List<MedProjection> getListMedCnam(
                     @Param("codFil") String codFil,
                     @Param("codAssur") String codAssur);

       @Query(value = """
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.cod_bord,
                            t.cod_assur,
                            t.num_soin,
                            t.ord_bult,
                            t.tot_honor,
                            t.tot_net,
                            t.tot_remb,
                            t.reg_remb,
                            t.cod_malad,
                            t.num_pec,
                            t.cod_fil,
                            t.num_assur,
                            t.dat_prev_accouch,
                            t.nat_bult,
                            t.mat_pers_conj,
                            t.num_ass_conj,
                            t.dat_saisie,
                            t.obs,
                            t.obs_a,
                            t.envoi,
                            t.ann_plaf_imp,
                            t.reg_adh,
                            t.mod_pay,
                            t.dat_vir,
                            p.mat_int,
                            p.dat_nais,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nomCompletPers,
                            a.lib_assur as libAssur,
                            r.lib_fill as libFill,
                            f.nom_pren as nomAdherent
                     from bult_soin t
                     inner join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     left join assurance a
                            on a.cod_assur = t.cod_assur
                     left join ref_filliere r
                            on r.cod_fil = t.cod_fil
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     where t.cod_soc = :soc
                       and t.nat_bult = 'C'
                     order by t.dat_saisie desc,
                              t.dat_soin desc
                     """, nativeQuery = true)
       List<BultSoinProjection> getBultCnam(@Param("soc") String soc);

       @Query(value = """
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.cod_bord,
                            t.cod_assur,
                            t.num_soin,
                            t.ord_bult,
                            t.tot_honor,
                            t.tot_net,
                            t.tot_remb,
                            t.reg_remb,
                            t.cod_malad,
                            t.num_pec,
                            t.cod_fil,
                            t.num_assur,
                            t.dat_prev_accouch,
                            t.nat_bult,
                            t.mat_pers_conj,
                            t.num_ass_conj,
                            t.dat_saisie,
                            t.obs,
                            t.obs_a,
                            t.envoi,
                            t.ann_plaf_imp,
                            t.reg_adh,
                            t.mod_pay,
                            t.dat_vir,
                            p.mat_int,
                            p.dat_nais,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nomCompletPers,
                            a.lib_assur as libAssur,
                            r.lib_fill as libFill,
                            f.nom_pren as nomAdherent
                     from bult_soin t
                     inner join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     left join assurance a
                            on a.cod_assur = t.cod_assur
                     left join ref_filliere r
                            on r.cod_fil = t.cod_fil
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     where t.cod_soc = :soc
                       and t.nat_bult = 'M'
                     order by t.dat_saisie desc,
                              t.dat_soin desc
                     """, nativeQuery = true)
       List<BultSoinProjection> getBultMut(@Param("soc") String soc);

       @Query(value = """
                     select *
                     from bord_envoi
                     where cod_soc = :soc
                       and typ_bord = 'C'
                       and coalesce(envoi_bord, 'O') = 'O'
                       and cod_bord is not null
                     order by dat_bord desc
                     """, nativeQuery = true)
       List<BordEnvoiPrejection> getBordtCnam(@Param("soc") String soc);

       @Query(value = """
                     select *
                     from bult_soin
                     where cod_soc = :soc
                       and nat_bult = 'C'
                       and coalesce(envoi, 'N') = 'O'
                       and cod_bord = :codBord
                     order by dat_saisie desc, dat_soin desc
                     """, nativeQuery = true)
       List<BultSoin> getBultCnamRecep(@Param("soc") String soc, @Param("codBord") String codBord);

       @Query(value = """
                     select t.mat_pers,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            t.cod_fil,
                            t.num_fam,
                            case
                              when t.num_fam = 0 then 'Adhérent'
                              else f.nom_pren
                            end as nom_pers,
                            t.dat_saisie,
                            t.dat_soin,
                            coalesce(pc.plafond, 0) - coalesce(pc.sold_plaf, 0) as solde,
                            t.tot_honor,
                            t.tot_net
                     from bult_soin t
                     join bord_envoi e
                          on t.cod_soc = e.cod_soc
                         and t.cod_bord  = e.cod_bord
                     join personnel p
                          on p.cod_soc = t.cod_soc
                         and p.mat_pers = t.mat_pers
                     left join famille f
                          on f.cod_soc = t.cod_soc
                         and f.mat_pers = t.mat_pers
                         and f.num_fam = t.num_fam
                     left join plafond_cnam pc
                          on pc.cod_soc = t.cod_soc
                         and pc.mat_pers = t.mat_pers
                         and pc.annee_cnam = extract(year from t.dat_soin)
                     where t.cod_soc = :codSoc
                       and e.cod_bord = :codBord
                     """, nativeQuery = true)
       List<BultSoinProjection> getBultSoinDetails(@Param("codSoc") String codSoc, @Param("codBord") String codBord);

       @Query(value = """
                     select t.mat_pers,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            t.cod_fil,
                            t.num_fam,
                            case
                                when t.num_fam = 0 then 'Adhérent'
                                else f.nom_pren
                            end as nom_pers,
                            t.dat_saisie,
                            t.dat_soin,
                            coalesce(pc.plafond, 0) - coalesce(pc.sold_plaf, 0) as solde,
                            t.tot_honor,
                            t.tot_net,
                            t.reg_adh
                       from bult_soin t
                       join bord_envoi e
                         on t.cod_soc = e.cod_soc
                        and t.cod_bord = e.cod_bord
                       join personnel p
                         on p.cod_soc = t.cod_soc
                        and p.mat_pers = t.mat_pers
                       left join famille f
                         on f.cod_soc = t.cod_soc
                        and f.mat_pers = t.mat_pers
                        and f.num_fam = t.num_fam
                       left join plafond_cnam pc
                         on pc.cod_soc = t.cod_soc
                        and pc.mat_pers = t.mat_pers
                        and pc.annee_cnam = extract(year from t.dat_soin)
                      where t.cod_soc = :cod_soc
                        and e.cod_bord = :cod_bord
                        and t.mod_pay = 'E'
                     """, nativeQuery = true)
       List<ControleBordCnamProjection> getPayBordMutuelle(
                     @Param("cod_soc") String cod_soc,
                     @Param("cod_bord") String cod_bord);

       @Query(value = """
                     select t.mat_pers,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            t.cod_fil,
                            t.num_fam,
                            case
                                when t.num_fam = 0 then 'Adhérent'
                                else f.nom_pren
                            end as nom_pers,
                            t.dat_saisie,
                            t.dat_soin,
                            coalesce(pc.plafond, 0) - coalesce(pc.sold_plaf, 0) as solde,
                            t.tot_honor,
                            t.tot_net,
                            t.reg_adh,
                            t.mod_pay,
                            t.dat_vir,
                            t.tot_remb
                       from bult_soin t
                       join bord_envoi e
                         on t.cod_soc = e.cod_soc
                        and t.cod_bord = e.cod_bord
                       join personnel p
                         on p.cod_soc = t.cod_soc
                        and p.mat_pers = t.mat_pers
                       left join famille f
                         on f.cod_soc = t.cod_soc
                        and f.mat_pers = t.mat_pers
                        and f.num_fam = t.num_fam
                       left join plafond_cnam pc
                         on pc.cod_soc = t.cod_soc
                        and pc.mat_pers = t.mat_pers
                        and pc.annee_cnam = extract(year from t.dat_soin)
                      where t.cod_soc = :cod_soc
                        and e.cod_bord = :cod_bord
                     """, nativeQuery = true)
       List<ControleBordCnamProjection> majModePayement(
                     @Param("cod_soc") String cod_soc,
                     @Param("cod_bord") String cod_bord);

       @Query(value = """
                     select t.mat_pers,
                     concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                     t.cod_fil,
                      t.num_fam,
                      case
                      when t.num_fam = 0 then 'Adhérent'
                      else f.nom_pren
                      end as nom_pers,
                      t.dat_saisie,
                      t.dat_soin,
                      coalesce(pc.plafond, 0) - coalesce(pc.sold_plaf, 0) as solde,
                      t.tot_honor,
                      t.tot_net,
                      t.reg_adh,
                      t.mod_pay,
                      t.dat_vir
                      from bult_soin t
                      join bord_envoi e
                      on t.cod_soc = e.cod_soc
                      and t.cod_bord = e.cod_bord
                      join personnel p
                      on p.cod_soc = t.cod_soc
                      and p.mat_pers = t.mat_pers
                      left join famille f
                      on f.cod_soc = t.cod_soc
                      and f.mat_pers = t.mat_pers
                      and f.num_fam = t.num_fam
                      left join plafond_cnam pc
                      on pc.cod_soc = t.cod_soc
                      and pc.mat_pers = t.mat_pers
                      and pc.annee_cnam = extract(year from t.dat_soin)
                      where t.cod_soc = :cod_soc
                      and e.cod_bord = :cod_bord
                      and t.mod_pay = 'V'
                      and coalesce(t.reg_adh, 'N') = 'N'
                      order by t.dat_saisie desc,
                      t.dat_soin desc,
                      t.cod_soc,
                      t.mat_pers,
                      t.num_fam
                      """, nativeQuery = true)
       List<ControleBordCnamProjection> getBultSoinCpt(
                     @Param("cod_soc") String cod_soc,
                     @Param("cod_bord") String cod_bord);

       @Query(value = """
                         select t.mat_pers,
                                concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                                t.cod_fil,
                                t.num_fam,
                                case
                                    when t.num_fam = 0 then 'Adhérent'
                                    else f.nom_pren
                                end as nom_pers,
                                t.dat_saisie,
                                t.dat_soin,
                                coalesce(pc.plafond, 0) - coalesce(pc.sold_plaf, 0) as solde,
                                t.tot_honor,
                                t.tot_net,
                                t.reg_adh,
                                t.mod_pay,
                                t.dat_vir
                           from bult_soin t
                           join bord_envoi e
                             on t.cod_soc = e.cod_soc
                            and t.cod_bord = e.cod_bord
                           join personnel p
                             on p.cod_soc = t.cod_soc
                            and p.mat_pers = t.mat_pers
                           left join famille f
                             on f.cod_soc = t.cod_soc
                            and f.mat_pers = t.mat_pers
                            and f.num_fam = t.num_fam
                           left join plafond_cnam pc
                             on pc.cod_soc = t.cod_soc
                            and pc.mat_pers = t.mat_pers
                            and pc.annee_cnam = extract(year from t.dat_soin)
                          where t.cod_soc = :cod_soc
                            and e.cod_bord = :cod_bord
                            and t.mod_pay = 'C'
                            and coalesce(t.reg_adh, 'N') = 'N'
                          order by t.dat_saisie desc,
                                   t.dat_soin desc,
                                   t.cod_soc,
                                   t.mat_pers,
                                   t.num_fam
                     """, nativeQuery = true)
       List<ControleBordCnamProjection> getBultSoinCheqCpt(
                     @Param("cod_soc") String cod_soc,
                     @Param("cod_bord") String cod_bord);

       @Query(value = "select * from bult_soin where cod_soc = :soc and mat_pers = :mat and num_fam = :numFam and dat_soin = :dat", nativeQuery = true)
       BultSoin getBultSoinById(@Param("soc") String soc, @Param("mat") String mat, @Param("numFam") String numFam,
                     @Param("dat") LocalDate dat);

}