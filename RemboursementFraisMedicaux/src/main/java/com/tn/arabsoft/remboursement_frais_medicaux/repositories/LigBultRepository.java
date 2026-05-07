package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBult;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigBultCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultProjection;

import java.time.LocalDate;
import java.util.List;

public interface LigBultRepository extends JpaRepository<LigBult, LigBultCle> {

        @Query(value = """
                        select *
                          from lig_bult
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBult> getLigBult(
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
                            t.num_lig,
                            t.prf_typ,
                            t.prf_cod,
                            t.dat_act,
                            t.indice,
                            t.mnt_honor,
                            t.mnt_net,
                            t.mnt_remb,
                            t.obs,
                            t.obs_a,
                            t.nbr_piece,
                            t.nbr_vign,
                            t.nat_act,
                            t.mtt_acte,
                            t.taux_act,
                            t.plafonne,
                            t.plafond,
                            t.a_indice,
                            t.ctr_duree,
                            t.duree_act,
                            t.imput_plaf,
                            a.lib_act,
                            e.etab_rsoc AS lib_etablis
                        from lig_bult t
                        left join acte a
                               on a.abrv_act = t.abrv_act
                        left join etabliss e
                               on e.prf_typ = t.prf_typ
                               and e.prf_cod = t.prf_cod
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                          and t.num_fam = :numFam
                          and t.dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultProjection> getLigBultCons(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        delete FROM lig_bult
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :fam
                           and dat_soin = :datSoin
                           and abrv_act = :abrv
                           and num_lig = :numLig
                        """, nativeQuery = true)
        void deleteLigBult(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") Integer fam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("abrv") String abrv,
                        @Param("numLig") Long numLig);

}