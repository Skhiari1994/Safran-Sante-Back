package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultApp;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultApp;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultAppProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigBultAppRepository extends JpaRepository<LigBultApp, CleLigBultApp> {

        @Query(value = """
                        select coalesce(max(coalesce(num_lig_app, 0)), 0) + 1
                          from lig_bult_app
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                           and cod_app = :codApp
                        """, nativeQuery = true)
        Long getNumLigApp(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Long numFam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("codApp") String codApp);

        @Query(value = """
                        select *
                          from lig_bult_app
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultApp> getLigBultApp(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Query(value = """
                        select
                            t.cod_soc,
                            t.mat_pers,
                            t.num_fam,
                            t.dat_soin,
                            t.abrv_act,
                            t.cod_app,
                            t.num_lig,
                            t.mnt_honor,
                            t.mnt_net,
                            t.mnt_remb,
                            t.accord_app,
                            t.indice,
                            t.dat_act,
                            t.prf_typ,
                            t.prf_cod,
                            t.num_pec_app,
                            t.num_lig_app,
                            t.mut_mnt_net,
                            r.lib_app,
                            concat(coalesce(e.etab_rsoc, ''), concat(' ', coalesce(e.pr_rsoc, ''))) as lib_etablis
                        from lig_bult_app t
                        left join ref_appareil r
                               on r.cod_app = t.cod_app
                        left join ref_etablis e
                               on e.prf_typ = t.prf_typ
                              and e.prf_cod = t.prf_cod
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                          and t.num_fam = :numFam
                          and t.dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultAppProjection> getLigBultAppCons(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_bult_app
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :fam
                           and dat_soin = :datSoin
                           and abrv_act = :abrv
                           and num_lig_app = :numLig
                           and cod_app = :codApp
                        """, nativeQuery = true)
        void deleteLigBultApp(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") Integer fam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("abrv") String abrv,
                        @Param("numLig") Long numLig,
                        @Param("codApp") String codApp);

}
