package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.BultArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.BultArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.BultArriverLibreCnamProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.BultArriverProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LovNumFilCnamLibre;

import java.time.LocalDate;
import java.util.List;

@Repository
@SuppressWarnings({ "java:S117" })
public interface BultArriverRepository extends JpaRepository<BultArriver, BultArriverCle> {

        @Query(value = """
                        select *
                        from bult_arriver
                        where cod_soc = :codSoc
                          and mat_pers = :matPers
                          and num_fam = :numFam
                          and dat_soin = :datSoin
                        """, nativeQuery = true)
        BultArriver findBultArriverById(
                        @Param("codSoc") String codSoc,
                        @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Query(value = """
                        select
                            ba.cod_soc,
                            p.num_retr,
                            ba.mat_pers,
                            ba.tot_net,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            ba.cod_fil,
                            ba.cod_bord,
                            ba.cod_assur,
                            ba.num_fam,
                            coalesce(f.nom_pren, 'Adhérent') as nom,
                            case
                                when ba.num_fam = 0 then 'Adhérent'
                                when ba.num_fam = 99 then 'Conjoint'
                                when ba.num_fam between 1 and 10 then 'Enfant'
                                else 'Autre'
                            end as statut_famille,
                            ba.dat_soin,
                            (
                                select rr.lib_remb
                                from regime_remb rr
                                where rr.reg_remb = ba.reg_remb
                            ) as lib_remb,
                            ba.reg_remb,
                            ba.tot_remb
                        from bult_soin ba
                        join personnel p
                          on ba.mat_pers = p.mat_pers
                         and ba.cod_soc = p.cod_soc
                        left join famille f
                            on ba.cod_soc = f.cod_soc
                           and ba.mat_pers = f.mat_pers
                           and ba.num_fam = f.num_fam
                        where ba.cod_soc = :cod_soc
                          and ba.cod_bord = :cod_bord
                        order by ba.dat_soin desc
                        """, nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBord(
                        @Param("cod_soc") String cod_soc,
                        @Param("cod_bord") String cod_bord);

        @Query(value = """
                        select
                            ba.cod_soc,
                            p.num_retr,
                            ba.mat_pers,
                            ba.tot_net,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            ba.cod_fil,
                            ba.cod_bord,
                            ba.cod_assur,
                            ba.num_fam,
                            coalesce(f.nom_pren, 'Adhérent') as nom,
                            case
                                when ba.num_fam = 0 then 'Adhérent'
                                when ba.num_fam = 99 then 'Conjoint'
                                when ba.num_fam between 1 and 10 then 'Enfant'
                                else 'Autre'
                            end as statut_famille,
                            ba.dat_soin,
                            (
                                select rr.lib_remb
                                from regime_remb rr
                                where rr.reg_remb = ba.reg_remb
                            ) as lib_remb,
                            ba.reg_remb,
                            ba.tot_remb
                        from bult_arriver ba
                        join personnel p
                          on ba.mat_pers = p.mat_pers
                         and ba.cod_soc = p.cod_soc
                        left join famille f
                            on ba.num_fam = f.num_fam
                           and ba.mat_pers = f.mat_pers
                           and ba.cod_soc = f.cod_soc
                        where ba.cod_bord = :cod_bord
                        order by ba.dat_soin desc
                        """, nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBord2(
                        @Param("cod_bord") String cod_bord);

        @Query(value = """
                        select
                            ba.cod_soc,
                            p.num_retr,
                            ba.mat_pers,
                            ba.tot_net,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            ba.cod_fil,
                            ba.cod_bord,
                            ba.cod_assur,
                            ba.num_fam,
                            coalesce(f.nom_pren, 'Adhérent') as nom,
                            case
                                when ba.num_fam = 0 then 'Adhérent'
                                when ba.num_fam = 99 then 'Conjoint'
                                when ba.num_fam between 1 and 10 then 'Enfant'
                                else 'Autre'
                            end as statut_famille,
                            ba.dat_soin,
                            ba.dat_saisie,
                            (
                                select rr.lib_remb
                                from regime_remb rr
                                where rr.reg_remb = ba.reg_remb
                            ) as lib_remb,
                            ba.tot_remb
                        from bult_soin ba
                        join personnel p
                            on ba.mat_pers = p.mat_pers
                           and ba.cod_soc = p.cod_soc
                        left join famille f
                            on ba.num_fam = f.num_fam
                           and ba.mat_pers = f.mat_pers
                           and ba.cod_soc = f.cod_soc
                        where ba.cod_bord = :cod_bord
                        order by ba.dat_saisie desc,
                                 ba.mat_pers,
                                 ba.num_fam
                        """, nativeQuery = true)
        List<BultArriverProjection> findBultEnvoiByCodBord(
                        @Param("cod_bord") String cod_bord);

        @Query(value = """
                        select
                            ba.cod_soc,
                            p.num_retr,
                            ba.mat_pers,
                            ba.tot_net,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            ba.cod_fil,
                            ba.cod_bord,
                            ba.cod_assur,
                            ba.num_fam,
                            coalesce(f.nom_pren, 'Adhérent') as nom,
                            case
                                when ba.num_fam = 0 then 'Adhérent'
                                when ba.num_fam = 99 then 'Conjoint'
                                when ba.num_fam between 1 and 10 then 'Enfant'
                                else 'Autre'
                            end as statut_famille,
                            ba.dat_soin,
                            (
                                select rr.lib_remb
                                from regime_remb rr
                                where rr.reg_remb = ba.reg_remb
                            ) as lib_remb,
                            ba.tot_remb,

                            -- total remboursement
                            (
                                select coalesce(sum(coalesce(mnt_remb, 0)), 0)
                                from lig_bult_arriver
                                where cod_soc = ba.cod_soc
                                  and mat_pers = ba.mat_pers
                                  and num_fam = ba.num_fam
                                  and dat_soin = ba.dat_soin
                            ) as tot_mut,

                            -- solde plafond (portable version)
                            (
                                select coalesce(plafond, 0) - coalesce(sold_plaf, 0)
                                from plafond_cnam
                                where cod_soc = ba.cod_soc
                                  and mat_pers = ba.mat_pers
                                  and annee_cnam = extract(year from ba.dat_soin)
                            ) as solde

                        from bult_arriver ba
                        join personnel p
                            on ba.mat_pers = p.mat_pers
                           and ba.cod_soc = p.cod_soc
                        left join famille f
                            on ba.num_fam = f.num_fam
                           and ba.mat_pers = f.mat_pers
                           and ba.cod_soc = f.cod_soc
                        where ba.cod_bord = :cod_bord
                        order by ba.dat_soin desc
                        """, nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBord4(
                        @Param("cod_bord") String cod_bord);

        @Query(value = """
                        select
                            ba.cod_soc,
                            p.num_retr,
                            ba.mat_pers,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            ba.cod_fil,
                            ba.cod_bord,
                            ba.cod_assur,
                            ba.num_fam,
                            coalesce(f.nom_pren, 'Adhérent') as nom,
                            case
                                when ba.num_fam = '0' then 'Adhérent'
                                when ba.num_fam = '99' then 'Conjoint'
                                when ba.num_fam between 1 and 10 then 'Enfant'
                                else 'Autre'
                            end as statut_famille,
                            ba.dat_soin,
                            (
                                select rr.lib_remb
                                from regime_remb rr
                                where rr.reg_remb = ba.reg_remb
                            ) as lib_remb,
                            ba.tot_remb
                        from
                            bult_soin ba
                        join personnel p on ba.mat_pers = p.mat_pers and ba.cod_soc = p.cod_soc
                        left join famille f on ba.cod_soc = f.cod_soc and ba.mat_pers = f.mat_pers and ba.num_fam = f.num_fam
                        where
                            ba.cod_bord = :cod_bord
                        order by ba.dat_soin desc
                        """, nativeQuery = true)
        List<BultArriverProjection> findBultArriverByCodBordRecep(@Param("cod_bord") String cod_bord);

        @Query(value = """
                        select
                            ba.dat_saisie as dat_saisie,
                            ba.cod_soc as cod_soc,
                            ba.mat_pers as mat_pers,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            ba.cod_fil as cod_fil,
                            ba.num_fam as num_fam,
                            ba.tot_net as tot_net,
                            coalesce(f.nom_pren, 'Adhérent') as nom,

                            case
                                when ba.num_fam = '0' then 'Adhérent'
                                when ba.num_fam = '99' then 'Conjoint'
                                when ba.num_fam between 1 and 10 then 'Enfant'
                                else 'Autre'
                            end as statut_famille,

                            ba.dat_soin as dat_soin,

                            (select rr.lib_remb
                             from regime_remb rr
                             where rr.reg_remb = ba.reg_remb) as lib_remb,

                            ba.tot_remb as tot_remb,

                            (select coalesce(sum(coalesce(mnt_remb,0)),0)
                             from lig_bult_arriver
                             where cod_soc = ba.cod_soc
                               and mat_pers = ba.mat_pers
                               and num_fam = ba.num_fam
                               and dat_soin = ba.dat_soin) as tot_mut,

                            case ba.reg_adh
                                when 'O' then 'Oui'
                                when 'N' then 'Non'
                                else ''
                            end as reg_adh,

                            (select coalesce(plafond,0) - coalesce(sold_plaf,0)
                             from plafond_cnam
                             where cod_soc = ba.cod_soc
                               and mat_pers = ba.mat_pers
                               and annee_cnam = extract(year from ba.dat_soin)) as solde,

                            ba.reclam as reclamation,
                            ba.mod_pay as mod_pay,
                            ba.dat_vir as dat_vir,
                            p.num_retr as num_retr,
                            ba.cod_assur as cod_assur,
                            ba.cod_bord as cod_bord,
                            ba.reg_remb as reg_remb

                        from bult_arriver ba
                        join personnel p on ba.cod_soc = p.cod_soc and ba.mat_pers = p.mat_pers
                        left join famille f on ba.cod_soc = f.cod_soc and ba.mat_pers = f.mat_pers and ba.num_fam = f.num_fam

                        where ba.cod_bord = :cod_bord
                        order by ba.dat_saisie desc, ba.mat_pers, ba.num_fam
                        """, nativeQuery = true)
        List<BultArriverProjection> findBultArriverReg(@Param("cod_bord") String cod_bord);

        @Query(value = """
                        select
                            t.ann_plaf_imp,
                            t.cod_assur,
                            t.cod_bord,
                            t.cod_fil,
                            t.cod_malad,
                            t.cod_soc,
                            t.dat_prev_accouch,
                            t.dat_saisie,
                            t.dat_soin,
                            t.dat_vir,
                            t.decis_med,
                            t.envoi,
                            t.mat_pers,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            p.num_retr,
                            case
                                when coalesce(f.nom_pren,'') <> '' then f.nom_pren
                                else
                                    case
                                        when t.num_fam = 0 then 'Adhérent'
                                        when t.num_fam = 99 then 'Conjoint'
                                        when t.num_fam between 1 and 10 then 'Enfant'
                                        else 'Autre'
                                    end
                            end as nom_pren,
                            t.mat_pers_conj,
                            t.mod_pay,
                            t.nat_bult,
                            t.num_assur,
                            t.num_ass_conj,
                            t.num_fam,
                            t.num_pec,
                            t.num_soin,
                            t.num_soin_cnam,
                            t.obs,
                            t.obs_a,
                            t.ord_bult,
                            t.reclam,
                            t.reg_adh,
                            t.reg_remb,
                            t.tot_honor,
                            t.tot_net,
                            t.tot_remb,
                            t.tot_remb_med,
                            t.typ_bult
                        from bult_arriver t
                        join personnel p on t.cod_soc = p.cod_soc and t.mat_pers = p.mat_pers
                        left join famille f on t.cod_soc = f.cod_soc AND t.mat_pers = f.mat_pers AND t.num_fam = f.mat_pers
                        where t.cod_bord = :cod_bord_
                        order by t.dat_saisie desc, t.mat_pers, t.num_fam
                        """, nativeQuery = true)
        List<BultArriverLibreCnamProjection> findBultArriverByCodBordLibreCnam(@Param("cod_bord") String cod_bord_);

        @Query(value = """
                        select
                            p.mat_pers,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            a.cod_fil,
                            p.num_retr
                        from personnel p
                        join pers_affil a on p.cod_soc = a.cod_soc and p.mat_pers = a.mat_pers
                        where p.cod_soc = :codSoc
                          and a.courant = 'O'
                        """, countQuery = """
                        select count(*)
                        from personnel p
                        join pers_affil a on p.cod_soc = a.cod_soc and p.mat_pers = a.mat_pers
                        where p.cod_soc = :codSoc
                          and a.courant = 'O'
                        """, nativeQuery = true)
        Page<LovNumFilCnamLibre> lovNumFilCnamLibre(@Param("codSoc") String codSoc, Pageable pageable);

        @Query(value = """
                        select
                            ba.cod_soc,
                            ba.mat_pers,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) as nom_prenom,
                            ba.cod_fil,
                            ba.num_fam,
                            coalesce(f.nom_pren, 'Adhérent') as nom,
                            case
                                when ba.num_fam = 0 then 'Adhérent'
                                when ba.num_fam = 99 then 'Conjoint'
                                when ba.num_fam between 1 and 10 then 'Enfant'
                                else 'autre'
                            end as statut_famille,
                            ba.dat_soin,
                            (select rr.lib_remb
                             from regime_remb rr
                             where rr.reg_remb = ba.reg_remb) as lib_remb,
                            ba.tot_remb,
                            (select coalesce(sum(coalesce(mnt_remb,0)),0)
                             from lig_bult_arriver
                             where cod_soc = ba.cod_soc
                               and mat_pers = ba.mat_pers
                               and num_fam = ba.num_fam
                               and dat_soin = ba.dat_soin) as tot_mut,
                            case ba.reg_adh
                                when 'O' then 'Oui'
                                when 'N' then 'Non'
                                else ''
                            end as reg_adh,
                            (select coalesce(plafond,0) - coalesce(sold_plaf,0)
                             from plafond_cnam
                             where cod_soc = ba.cod_soc
                               and mat_pers = ba.mat_pers
                               and annee_cnam = extract(year from ba.dat_soin)) as solde,
                            ba.reclam as reclamation,
                            ba.mod_pay,
                            ba.dat_vir
                        from bult_arriver ba
                        join personnel p on ba.cod_soc = p.cod_soc and ba.mat_pers = p.mat_pers
                        left join famille f on ba.cod_soc = f.cod_soc and ba.mat_pers = f.mat_pers and ba.num_fam = f.num_fam
                        where ba.cod_bord = :cod_bord
                          and mod_pay = 'V'
                          and coalesce(reg_adh, 'N') = 'N'
                        order by dat_saisie desc, mat_pers, num_fam
                        """, nativeQuery = true)
        List<BultArriverProjection> findBultArriverCptCnam(@Param("cod_bord") String cod_bord);

        @Transactional
        @Modifying
        @Query(value = """
                        delete from bult_arriver
                        where cod_soc = :soc
                          and mat_pers = :mat
                          and num_fam = :fam
                          and dat_soin = :datSoin
                        """, nativeQuery = true)
        void deleteBulletin(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") String fam,
                        @Param("datSoin") LocalDate datSoin);

}
