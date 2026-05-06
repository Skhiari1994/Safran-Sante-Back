package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigAppArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigAppArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigAppArriverProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigAppArriverRepository extends JpaRepository<LigAppArriver, LigAppArriverCle> {

        @Query(value = """
                        select lbv.dat_act,
                               lbv.cod_app,
                               a.lib_app,
                               lbv.mnt_honor,
                               lbv.mnt_remb,
                               case lbv.prf_typ
                                   when 1 then 'Personne physique'
                                   when 2 then 'Etablissement'
                                   else 'Autre'
                               end as prf_type,
                               lbv.prf_typ,
                               concat(re.etab_rsoc, concat(' ', re.pr_rsoc)) as lib_org,
                               lbv.prf_cod,
                               lbv.accord_app,
                               lbv.dat_soin,
                               lbv.cod_soc,
                               lbv.num_fam,
                               lbv.mat_pers,
                               lbv.abrv_act,
                               lbv.num_pec_app,
                               lbv.num_lig
                        from lig_app_arriver lbv
                        left join ref_appareil a
                                on a.cod_app = lbv.cod_app
                        left join ref_etablis re
                                on re.prf_cod = lbv.prf_cod
                        where lbv.cod_soc = :cod_soc
                          and lbv.mat_pers = :mat_pers
                          and lbv.num_fam = :num_fam
                          and lbv.dat_soin = :dat_soin
                        """, nativeQuery = true)
        List<LigAppArriverProjection> findLigAppArriverById(
                        @Param("cod_soc") String codSoc,
                        @Param("mat_pers") String matPers,
                        @Param("num_fam") Integer numFam,
                        @Param("dat_soin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_app_arriver
                        where cod_soc = :soc
                          and mat_pers = :mat
                          and num_fam = :fam
                          and dat_soin = :datSoin
                          and abrv_act = :abrv
                          and num_lig = :numLig
                          and cod_app = :codApp
                        """, nativeQuery = true)
        void deleteLigAppArriver(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") String fam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("abrv") String abrv,
                        @Param("numLig") String numLig,
                        @Param("codApp") String codApp);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_app_arriver
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

        @Query("""
                        select max(l.num_lig)
                        from LigAppArriver l
                        where l.cod_soc = :codSoc
                          and l.mat_pers = :matPers
                          and l.num_fam = :numFam
                          and l.dat_soin = :datSoin
                        """)
        Integer findMaxNumLig(
                        @Param("codSoc") String codSoc,
                        @Param("matPers") String matPers,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

}
