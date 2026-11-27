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
                         p.dat_nais,
                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_complet_pers,
                         a.lib_assur as lib_assur,
                         p.num_retr as num_retr,
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
                         p.num_retr as num_retr,
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
                         p.num_retr as num_retr,
                         p.cod_retr as cod_retr,
                         r.lib_fill as lib_fill,
                         f.nom_pren as nom_adherent
                     from bult_soin t

                     left join personnel p
                            on t.cod_soc = p.cod_soc
                           and t.mat_pers = p.mat_pers

                     left join assurance a
                            on t.cod_assur = a.cod_assur

                     left join ref_filliere r
                            on t.cod_fil = r.cod_fil

                     left join famille f
                            on t.cod_soc = f.cod_soc
                           and t.mat_pers = f.mat_pers
                           and t.num_fam = f.num_fam

                     where t.nat_bult = 'C'
                       and coalesce(t.envoi, 'N') = 'O'
                       and t.cod_soc = :soc
                       and (:mat is null or t.mat_pers = :mat)
                       and (:num_fam is null or t.num_fam = :num_fam)
                       and (:dat_soin is null or t.dat_soin = :dat_soin)
                     order by t.dat_saisie desc, t.dat_soin desc, t.mat_pers, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> getBulletinCnamCons(@Param("soc") String soc, @Param("mat") String mat,
                     @Param("num_fam") Integer num_fam, @Param("dat_soin") LocalDate dat_soin);

       /**
        * Liste légère (5 champs) des bulletins CNAM consultables, pour la fenêtre de
        * recherche.
        */
       @Query(value = """
                     select
                         t.cod_soc,
                         t.mat_pers,
                         t.num_fam,
                         t.dat_soin,
                         t.cod_bord,
                         coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe
                     from bult_soin t
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     left join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     where t.nat_bult = 'C'
                       and coalesce(t.envoi, 'N') = 'O'
                       and t.cod_soc = :soc
                     order by t.cod_bord, cast(t.mat_pers as numeric), t.dat_saisie desc, t.dat_soin desc, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinListeProjection> getBulletinCnamListe(@Param("soc") String soc);

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

                         concat(coalesce(p.nom_pers, ''), concat(' ', coalesce(p.pren_pers, ''))) as nom_complet_pers,

                         a.lib_assur as lib_assur,

                         coalesce(f.nom_pren, 'Adhérent') as nom_adherent

                     from bult_soin t

                     join personnel p
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
                       and t.cod_bord_assur = :codBord
                     order by t.dat_saisie desc,
                              t.mat_pers,
                              t.num_fam
                     """, nativeQuery = true)
       /**
        * Bulletins d'un bordereau mutuelle. Dans la base SQL Server, le rattachement
        * au bordereau mutuelle est porté par
        * COD_BORD_ASSUR (COD_BORD est vide pour les bulletins mutuelle et ne contient
        * que le bordereau CNAM) ; un
        * bordereau mutuelle regroupe les bulletins de toute nature d'une même société
        * (le nombre et les totaux de
        * BORD_ENVOI correspondent à ce regroupement).
        */
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
                         and pc.annee_cnam = year(t.dat_soin)
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
                        and t.cod_bord_assur = e.cod_bord
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
                        and pc.annee_cnam = year(t.dat_soin)
                      where t.cod_soc = :cod_soc
                        and e.cod_bord = :cod_bord
                     """, nativeQuery = true)
       List<ControleBordCnamProjection> getControleBordMutuelle(
                     @Param("cod_soc") String cod_soc,
                     @Param("cod_bord") String cod_bord);

       // libellé vide dans REF_ACT (ligne générique 000000000) : celui de l'acte ; ligne générique en premier, ordre stable
       @Query(value = """
                     select coalesce(nullif(ltrim(rtrim(r.lib_act)), ''), a.lib_act) as lib_act,
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
                     order by r.abrv_act,
                              case when r.cod_act = '000000000' then 0 else 1 end,
                              r.cod_act
                     """, nativeQuery = true)
       List<ActProjection> getActes(
                     @Param("parente") String parente,
                     @Param("sexe") String sexe,
                     @Param("codFil") String codFil,
                     @Param("codAssur") String codAssur);

       @Query(value = """
                     select r.cod_act,
                            coalesce(nullif(ltrim(rtrim(r.lib_act)), ''), a.lib_act) as lib_act,
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
                     order by r.abrv_act,
                              case when r.cod_act = '000000000' then 0 else 1 end,
                              r.cod_act
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
                     where b.cod_fil = :codFil
                       and b.cod_assur = :codAssur
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
                            p.dat_nais,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            a.lib_assur as lib_assur,
                            p.num_retr as num_retr,
                            r.lib_fill as lib_fill,
                            f.nom_pren as nom_adherent
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
                       and t.cod_bord is null
                     order by t.dat_saisie desc,
                              t.dat_soin desc
                     """, nativeQuery = true)
       List<BultSoinProjection> getBultCnam(@Param("soc") String soc);

       @Query(value = """
                     select
                         ba.cod_soc,
                         ba.mat_pers,
                         ba.num_fam,
                         ba.dat_soin,
                         ba.num_soin,
                         ba.dat_saisie,
                         ba.cod_fil,
                         ba.cod_bord,
                         ba.cod_assur,
                         p.num_retr,
                         concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_complet_pers,
                         coalesce(f.nom_pren, 'Adhérent') as nom_adherent,
                         case
                             when ba.num_fam = 0 then 'Adhérent'
                             when ba.num_fam = 99 then 'Conjoint'
                             when ba.num_fam between 1 and 10 then 'Enfant'
                             else 'Autre'
                         end as statut_famille,
                        rr.lib_remb as lib_remb,
                         ba.tot_honor,
                         ba.tot_net,
                         ba.tot_remb
                     from bult_soin ba
                     join personnel p
                         on ba.mat_pers = p.mat_pers
                        and ba.cod_soc = p.cod_soc
                     left join famille f
                         on ba.num_fam = f.num_fam
                        and ba.mat_pers = f.mat_pers
                        and ba.cod_soc = f.cod_soc
                     left join regime_remb rr
                     on ba.reg_remb = rr.reg_remb
                     where ba.cod_soc = :codSoc
                       and ba.cod_assur = :codAssur
                       and ba.cod_bord = :codBord
                     order by ba.dat_saisie desc,
                              ba.mat_pers,
                              ba.num_fam
                     """, nativeQuery = true)
       List<BultSoinProjection> findBultEnvoiBySocAssurBord(@Param("codSoc") String codSoc,
                     @Param("codAssur") String codAssur,
                     @Param("codBord") String codBord);

       @Query(value = """
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            coalesce(nullif(trim(t.cod_bord_assur), ''), t.cod_bord) as cod_bord,
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
                            p.dat_nais,
                            concat(concat(p.nom_pers, ' '), p.pren_pers) as nom_complet_pers,
                            a.lib_assur as lib_assur,
                            p.num_retr as num_retr,
                            p.cod_retr as cod_retr,
                            r.lib_fill as lib_fill,
                            f.nom_pren as nom_adherent
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
                       and (:mat = '' or t.mat_pers = :mat)
                       and (:num_fam is null or t.num_fam = :num_fam)
                       and (:dat_soin is null or t.dat_soin = :dat_soin)
                     order by t.dat_saisie desc,
                              t.dat_soin desc
                     """, nativeQuery = true)
       List<BultSoinProjection> getBultMutFiltre(@Param("soc") String soc, @Param("mat") String mat,
                     @Param("num_fam") Integer num_fam, @Param("dat_soin") LocalDate dat_soin);

       /**
        * Tous les bulletins mutuelle de la société (sans filtre). num_fam est
        * numérique : null = pas de filtre.
        */
       default List<BultSoinProjection> getBultMut(String soc) {
              return getBultMutFiltre(soc, "", null, null);
       }

       /**
        * Liste légère (5 champs) des bulletins mutuelle, pour la fenêtre de recherche.
        */
       @Query(value = """
                     select
                         t.cod_soc,
                         t.mat_pers,
                         t.num_fam,
                         t.dat_soin,
                         coalesce(nullif(trim(t.cod_bord_assur), ''), t.cod_bord) as cod_bord,
                         coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe
                     from bult_soin t
                     inner join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     where t.cod_soc = :soc
                       and t.nat_bult = 'M'
                     order by t.dat_saisie desc, t.dat_soin desc, t.mat_pers, t.num_fam
                     """, nativeQuery = true)
       List<BultSoinListeProjection> getBultMutListe(@Param("soc") String soc);

       /*
        * Bulletins CNAM envoyés pour la fenêtre de recherche, page par page : même liste que getBulletinCnamListe
        * (mêmes colonnes, mêmes conditions), filtrée et découpée par la base. Ordre = colonnes de la fenêtre de gauche
        * à droite (code bordereau, matricule, n° famille, date de soin), puis num_soin : ordre stable, d'une page à
        * l'autre et pour les boutons fiche précédente / suivante.
        * mat : égalité exacte ; cod_bord, num_fam, dat_soin (jj/mm/aaaa) : « contient » ; m1..m3 / l1..l3 : mots de la
        * recherche globale (chacun doit se retrouver dans une des colonnes) ; sexe : 'M' ou 'F'.
        */
       @Query(value = """
                     select q.cod_soc, q.mat_pers, q.num_fam, q.dat_soin, q.cod_bord, q.sexe
                     from (
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.cod_bord,
                            coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe,
                            t.num_soin
                     from bult_soin t
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     left join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     where t.nat_bult = 'C'
                       and coalesce(t.envoi, 'N') = 'O'
                       and t.cod_soc = :soc
                     ) q
                     where (:mat is null or lower(ltrim(rtrim(q.mat_pers))) = :mat)
                       and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                       and (:num_fam is null or cast(q.num_fam as varchar(10)) like :num_fam escape '!')
                       and (:dat_soin is null or convert(varchar(10), q.dat_soin, 103) like :dat_soin escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:m1 is null or lower(ltrim(rtrim(q.mat_pers))) = :m1
                            or lower(q.cod_bord) like :l1 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l1 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l1 escape '!')
                       and (:m2 is null or lower(ltrim(rtrim(q.mat_pers))) = :m2
                            or lower(q.cod_bord) like :l2 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l2 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l2 escape '!')
                       and (:m3 is null or lower(ltrim(rtrim(q.mat_pers))) = :m3
                            or lower(q.cod_bord) like :l3 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l3 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l3 escape '!')
                     order by q.cod_bord,
                              case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end,
                              q.mat_pers, q.num_fam, q.dat_soin, q.num_soin
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<BultSoinListeProjection> pageBultCnam(@Param("soc") String soc, @Param("mat") String mat,
                     @Param("cod_bord") String cod_bord, @Param("num_fam") String num_fam,
                     @Param("dat_soin") String dat_soin, @Param("sexe") String sexe,
                     @Param("m1") String m1, @Param("l1") String l1, @Param("m2") String m2, @Param("l2") String l2,
                     @Param("m3") String m3, @Param("l3") String l3,
                     @Param("debut") int debut, @Param("taille") int taille);

       @Query(value = """
                     select count(*)
                     from (
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.cod_bord,
                            coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe,
                            t.num_soin
                     from bult_soin t
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     left join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     where t.nat_bult = 'C'
                       and coalesce(t.envoi, 'N') = 'O'
                       and t.cod_soc = :soc
                     ) q
                     where (:mat is null or lower(ltrim(rtrim(q.mat_pers))) = :mat)
                       and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                       and (:num_fam is null or cast(q.num_fam as varchar(10)) like :num_fam escape '!')
                       and (:dat_soin is null or convert(varchar(10), q.dat_soin, 103) like :dat_soin escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:m1 is null or lower(ltrim(rtrim(q.mat_pers))) = :m1
                            or lower(q.cod_bord) like :l1 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l1 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l1 escape '!')
                       and (:m2 is null or lower(ltrim(rtrim(q.mat_pers))) = :m2
                            or lower(q.cod_bord) like :l2 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l2 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l2 escape '!')
                       and (:m3 is null or lower(ltrim(rtrim(q.mat_pers))) = :m3
                            or lower(q.cod_bord) like :l3 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l3 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l3 escape '!')
                     """, nativeQuery = true)
       long compterBultCnam(@Param("soc") String soc, @Param("mat") String mat,
                     @Param("cod_bord") String cod_bord, @Param("num_fam") String num_fam,
                     @Param("dat_soin") String dat_soin, @Param("sexe") String sexe,
                     @Param("m1") String m1, @Param("l1") String l1, @Param("m2") String m2, @Param("l2") String l2,
                     @Param("m3") String m3, @Param("l3") String l3);

       /** Effectifs du filtre Sexe (sur toute la liste, comme avant) : Tous, Masculin, Féminin. */
       @Query(value = """
                     select cast(count(*) as bigint) as tous,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'M' then 1 else 0 end), 0) as bigint) as m,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'F' then 1 else 0 end), 0) as bigint) as f
                     from (
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.cod_bord,
                            coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe,
                            t.num_soin
                     from bult_soin t
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     left join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     where t.nat_bult = 'C'
                       and coalesce(t.envoi, 'N') = 'O'
                       and t.cod_soc = :soc
                     ) q
                     """, nativeQuery = true)
       ComptesSexeProjection sexesBultCnam(@Param("soc") String soc);

       /*
        * Bulletins mutuelle pour la fenêtre de recherche, page par page : même liste que getBultMutListe
        * (mêmes colonnes, mêmes conditions), filtrée et découpée par la base. Ordre = colonnes de la fenêtre de gauche
        * à droite (code bordereau, matricule, n° famille, date de soin), puis num_soin : ordre stable, d'une page à
        * l'autre et pour les boutons fiche précédente / suivante.
        * mat : égalité exacte ; cod_bord, num_fam, dat_soin (jj/mm/aaaa) : « contient » ; m1..m3 / l1..l3 : mots de la
        * recherche globale (chacun doit se retrouver dans une des colonnes) ; sexe : 'M' ou 'F'.
        */
       @Query(value = """
                     select q.cod_soc, q.mat_pers, q.num_fam, q.dat_soin, q.cod_bord, q.sexe
                     from (
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            coalesce(nullif(trim(t.cod_bord_assur), ''), t.cod_bord) as cod_bord,
                            coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe,
                            t.num_soin
                     from bult_soin t
                     inner join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     where t.cod_soc = :soc
                       and t.nat_bult = 'M'
                     ) q
                     where (:mat is null or lower(ltrim(rtrim(q.mat_pers))) = :mat)
                       and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                       and (:num_fam is null or cast(q.num_fam as varchar(10)) like :num_fam escape '!')
                       and (:dat_soin is null or convert(varchar(10), q.dat_soin, 103) like :dat_soin escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:m1 is null or lower(ltrim(rtrim(q.mat_pers))) = :m1
                            or lower(q.cod_bord) like :l1 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l1 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l1 escape '!')
                       and (:m2 is null or lower(ltrim(rtrim(q.mat_pers))) = :m2
                            or lower(q.cod_bord) like :l2 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l2 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l2 escape '!')
                       and (:m3 is null or lower(ltrim(rtrim(q.mat_pers))) = :m3
                            or lower(q.cod_bord) like :l3 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l3 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l3 escape '!')
                     order by q.cod_bord,
                              case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end,
                              q.mat_pers, q.num_fam, q.dat_soin, q.num_soin
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<BultSoinListeProjection> pageBultMut(@Param("soc") String soc, @Param("mat") String mat,
                     @Param("cod_bord") String cod_bord, @Param("num_fam") String num_fam,
                     @Param("dat_soin") String dat_soin, @Param("sexe") String sexe,
                     @Param("m1") String m1, @Param("l1") String l1, @Param("m2") String m2, @Param("l2") String l2,
                     @Param("m3") String m3, @Param("l3") String l3,
                     @Param("debut") int debut, @Param("taille") int taille);

       @Query(value = """
                     select count(*)
                     from (
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            coalesce(nullif(trim(t.cod_bord_assur), ''), t.cod_bord) as cod_bord,
                            coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe,
                            t.num_soin
                     from bult_soin t
                     inner join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     where t.cod_soc = :soc
                       and t.nat_bult = 'M'
                     ) q
                     where (:mat is null or lower(ltrim(rtrim(q.mat_pers))) = :mat)
                       and (:cod_bord is null or lower(q.cod_bord) like :cod_bord escape '!')
                       and (:num_fam is null or cast(q.num_fam as varchar(10)) like :num_fam escape '!')
                       and (:dat_soin is null or convert(varchar(10), q.dat_soin, 103) like :dat_soin escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:m1 is null or lower(ltrim(rtrim(q.mat_pers))) = :m1
                            or lower(q.cod_bord) like :l1 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l1 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l1 escape '!')
                       and (:m2 is null or lower(ltrim(rtrim(q.mat_pers))) = :m2
                            or lower(q.cod_bord) like :l2 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l2 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l2 escape '!')
                       and (:m3 is null or lower(ltrim(rtrim(q.mat_pers))) = :m3
                            or lower(q.cod_bord) like :l3 escape '!'
                            or cast(q.num_fam as varchar(10)) like :l3 escape '!'
                            or convert(varchar(10), q.dat_soin, 103) like :l3 escape '!')
                     """, nativeQuery = true)
       long compterBultMut(@Param("soc") String soc, @Param("mat") String mat,
                     @Param("cod_bord") String cod_bord, @Param("num_fam") String num_fam,
                     @Param("dat_soin") String dat_soin, @Param("sexe") String sexe,
                     @Param("m1") String m1, @Param("l1") String l1, @Param("m2") String m2, @Param("l2") String l2,
                     @Param("m3") String m3, @Param("l3") String l3);

       /** Effectifs du filtre Sexe (sur toute la liste, comme avant) : Tous, Masculin, Féminin. */
       @Query(value = """
                     select cast(count(*) as bigint) as tous,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'M' then 1 else 0 end), 0) as bigint) as m,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'F' then 1 else 0 end), 0) as bigint) as f
                     from (
                     select t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            coalesce(nullif(trim(t.cod_bord_assur), ''), t.cod_bord) as cod_bord,
                            coalesce(nullif(trim(f.sexe), ''), p.sexe) as sexe,
                            t.num_soin
                     from bult_soin t
                     inner join personnel p
                            on p.cod_soc = t.cod_soc
                           and p.mat_pers = t.mat_pers
                     left join famille f
                            on f.cod_soc = t.cod_soc
                           and f.mat_pers = t.mat_pers
                           and f.num_fam = t.num_fam
                     where t.cod_soc = :soc
                       and t.nat_bult = 'M'
                     ) q
                     """, nativeQuery = true)
       ComptesSexeProjection sexesBultMut(@Param("soc") String soc);

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
                         and pc.annee_cnam = year(t.dat_soin)
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
                        and t.cod_bord_assur = e.cod_bord
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
                        and pc.annee_cnam = year(t.dat_soin)
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
                        and t.cod_bord_assur = e.cod_bord
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
                        and pc.annee_cnam = year(t.dat_soin)
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
                      and t.cod_bord_assur = e.cod_bord
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
                      and pc.annee_cnam = year(t.dat_soin)
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
                            and t.cod_bord_assur = e.cod_bord
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
                            and pc.annee_cnam = year(t.dat_soin)
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

       @Query(value = """
                     SELECT b.*,
                            a.lib_assur AS lib_assur,
                            rr.lib_remb AS lib_remb,
                            CASE
                                WHEN b.num_fam = '0' OR b.num_fam IS NULL THEN 'Adhérent'
                                ELSE COALESCE(f.nom_pren, 'Adhérent')
                            END AS nom_adherent
                     FROM bult_soin b
                     LEFT JOIN assurance a ON a.cod_assur = b.cod_assur
                     LEFT JOIN regime_remb rr ON rr.reg_remb = b.reg_remb
                     LEFT JOIN famille f ON f.cod_soc = b.cod_soc
                                        AND f.mat_pers = b.mat_pers
                                        AND f.num_fam = b.num_fam
                     WHERE b.cod_soc = :codSoc
                       AND b.mat_pers = :matPers
                       AND (:natBult IS NULL OR :natBult = '' OR b.nat_bult = :natBult)
                       AND b.dat_soin >= :debutAnnee
                       AND b.dat_soin < :finAnnee
                     ORDER BY b.dat_soin DESC
                     """, nativeQuery = true)
       List<BultSoinProjection> findBulletinsConsultation(
                     @Param("codSoc") String codSoc,
                     @Param("matPers") String matPers,
                     @Param("natBult") String natBult,
                     @Param("debutAnnee") java.time.LocalDate debutAnnee,
                     @Param("finAnnee") java.time.LocalDate finAnnee);

       @Query(value = "select coalesce(max(num_soin), 0) + 1 from bult_soin", nativeQuery = true)
       Long nextNumSoin();

}
