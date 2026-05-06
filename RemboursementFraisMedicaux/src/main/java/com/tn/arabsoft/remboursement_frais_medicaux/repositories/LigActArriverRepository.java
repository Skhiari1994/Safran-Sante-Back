package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigActArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigActArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigActArriverProjection;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings({ "java:S107" })
public interface LigActArriverRepository extends JpaRepository<LigActArriver, LigActArriverCle> {

       @Query(value = """
                     select lbv.dat_act,
                            lbv.cod_act,
                            a.lib_act,
                            lbv.indice,
                            lbv.mnt_honor,
                            lbv.mnt_remb,
                            lbv.mut_mnt_net,
                            lbv.accord_act,
                            lbv.num_pec_act,
                            case lbv.prf_typ
                                when '1' then 'Personne physique'
                                when '2' then 'Etablissement'
                                else 'Autre'
                            end as prf_type,
                            lbv.prf_typ as type_etablis,
                            concat(re1.etab_rsoc, concat(' ', re1.pr_rsoc)) as lib_etablis,
                            concat(re2.etab_rsoc, concat(' ', re2.pr_rsoc)) as lib_org,
                            lbv.dat_soin,
                            lbv.cod_soc,
                            lbv.num_fam,
                            lbv.mat_pers,
                            lbv.dat_act,
                            lbv.num_lig,
                            lbv.abrv_act,
                            lbv.prf_cod
                     from lig_act_arriver lbv
                     left join ref_act a
                            on a.cod_act = lbv.cod_act
                     left join ref_etablis re1
                            on re1.prf_typ = lbv.prf_typ
                           and re1.prf_cod = lbv.prf_cod
                     left join ref_etablis re2
                            on re2.prf_cod = lbv.prf_cod
                     where lbv.cod_soc = :cod_soc
                       and lbv.mat_pers = :mat_pers
                       and lbv.num_fam = :num_fam
                       and lbv.dat_soin = :dat_soin
                     """, nativeQuery = true)
       List<LigActArriverProjection> findLigActArriverById(
                     @Param("cod_soc") String codSoc,
                     @Param("mat_pers") String matPers,
                     @Param("num_fam") Integer numFam,
                     @Param("dat_soin") LocalDate datSoin);

       @Modifying
       @Transactional
       @Query(value = """
                     delete from lig_act_arriver
                     where cod_soc = :soc
                       and mat_pers = :mat
                       and num_fam = :fam
                       and dat_soin = :datSoin
                       and abrv_act = :abrv
                       and num_lig = :numLig
                       and dat_act = :datAct
                       and cod_act = :codAct
                     """, nativeQuery = true)
       void deleteLigActArriver(
                     @Param("soc") String soc,
                     @Param("mat") String mat,
                     @Param("fam") String fam,
                     @Param("datSoin") LocalDate datSoin,
                     @Param("abrv") String abrv,
                     @Param("numLig") String numLig,
                     @Param("datAct") LocalDate datAct,
                     @Param("codAct") String codAct);

       @Modifying
       @Transactional
       @Query(value = """
                     delete from lig_act_arriver
                     where cod_soc = :codSoc
                       and mat_pers = :matPers
                       and num_fam = :numFam
                       and dat_soin = :datSoin
                     """, nativeQuery = true)
       void deleteByBulletin(
                     @Param("codSoc") String codSoc,
                     @Param("matPers") String matPers,
                     @Param("numFam") Integer numFam,
                     @Param("datSoin") LocalDate datSoin);

       @Modifying
       @Transactional
       @Query(value = """
                     insert into lig_act_arriver (
                         cod_soc,
                         mat_pers,
                         num_fam,
                         dat_soin,
                         indice,
                         abrv_act,
                         cod_act,
                         num_lig,
                         let_cod,
                         cot_act,
                         act_prix,
                         mnt_honor,
                         mnt_remb,
                         accord_act,
                         mnt_net,
                         dat_act,
                         prf_typ,
                         prf_cod,
                         mut_mnt_net,
                         num_pec_act,
                         decis_act
                     ) values (
                         :#{#acte.cod_soc},
                         :#{#acte.mat_pers},
                         :#{#acte.num_fam},
                         :#{#acte.dat_soin},
                         :#{#acte.indice},
                         :#{#acte.abrv_act},
                         :#{#acte.cod_act},
                         :#{#acte.num_lig},
                         :#{#acte.let_cod},
                         :#{#acte.cot_act},
                         :#{#acte.act_prix},
                         :#{#acte.mnt_honor},
                         :#{#acte.mnt_remb},
                         :#{#acte.accord_act},
                         :#{#acte.mnt_net},
                         :#{#acte.dat_act},
                         :#{#acte.prf_typ},
                         :#{#acte.prf_cod},
                         :#{#acte.mut_mnt_net},
                         :#{#acte.num_pec_act},
                         :#{#acte.decis_act}
                     )
                     """, nativeQuery = true)
       void insertActe(@Param("acte") LigActArriver acte);

       @Query("select max(l.num_lig) from LigActArriver l where l.cod_soc = :codSoc and l.mat_pers = :matPers and l.num_fam = :numFam and l.dat_soin = :datSoin")
       Integer findMaxNumLig(String codSoc, String matPers, Integer numFam, java.time.LocalDate datSoin);
}
