package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigBultArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigBultArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigBultArriverProjection;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings({ "java:S117" })
public interface LigBultArriverRepository extends JpaRepository<LigBultArriver, CleLigBultArriver> {

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
                            a.lib_act
                        from lig_bult_arriver t
                        left join acte a
                               on a.abrv_act = t.abrv_act
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                          and t.num_fam = :numFam
                          and t.dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigBultArriverProjection> getLigBultArriver(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        update bord_arriver b
                           set b.clot_bord = 'O'
                         where b.cod_soc = :soc
                           and b.cod_assur = :cod_assur
                           and b.cod_bord = :cod_bord
                        """, nativeQuery = true)
        void updateBordArriver(
                        @Param("soc") String soc,
                        @Param("cod_assur") String cod_assur,
                        @Param("cod_bord") String cod_bord);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_bult_arriver
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                           and num_lig = :numLig
                        """, nativeQuery = true)
        void deleteLigBultArriver(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("numLig") Long numLig);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_bult_arriver
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
}
