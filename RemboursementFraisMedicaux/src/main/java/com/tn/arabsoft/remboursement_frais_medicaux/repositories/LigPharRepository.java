package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigPhar;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleLigPhar;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigPharProjection;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigPharProjectionCons;

import java.time.LocalDate;
import java.util.List;

public interface LigPharRepository extends JpaRepository<LigPhar, CleLigPhar> {

        @Query(value = """
                        select
                            r.lib_med,
                            r.cod_med,
                            r.mdc_prix,
                            r.med_prix,
                            r.prix_remb,
                            r.abrv_act
                        from ref_med r
                        inner join acte a
                                on r.abrv_act = a.abrv_act
                        inner join bareme_remb b
                                on b.abrv_act = a.abrv_act
                        where b.cod_fil = :codFil
                          and b.cod_assur = :codAssur
                        """, nativeQuery = true)
        List<LigPharProjection> getMed(
                        @Param("codFil") String codFil,
                        @Param("codAssur") String codAssur);

        @Query(value = """
                        select *
                          from lig_phar
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :numFam
                           and dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigPhar> getLigPhar(
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
                            t.cod_med,
                            t.num_lig,
                            t.indice,
                            t.dat_act,
                            t.prf_typ,
                            t.prf_cod,
                            t.mnt_honor,
                            t.mnt_net,
                            t.mnt_remb,
                            t.mdc_prix,
                            t.med_prix,
                            t.prix_remb,
                            t.obs,
                            t.obs_a,
                            t.nbr_piece,
                            t.nbr_vign,
                            t.abrv_act,
                            rm.lib_med,
                            e.etab_rsoc as lib_etablis
                        from lig_phar t
                        left join ref_med rm
                               on rm.cod_med = t.cod_med
                        left join etabliss e
                               on e.prf_typ = t.prf_typ
                              and e.prf_cod = t.prf_cod
                        where t.cod_soc = :soc
                          and t.mat_pers = :mat
                          and t.num_fam = :numFam
                          and t.dat_soin = :datSoin
                        """, nativeQuery = true)
        List<LigPharProjectionCons> getLigPharCons(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("numFam") Integer numFam,
                        @Param("datSoin") LocalDate datSoin);

        @Modifying
        @Transactional
        @Query(value = """
                        delete from lig_phar
                         where cod_soc = :soc
                           and mat_pers = :mat
                           and num_fam = :fam
                           and dat_soin = :datSoin
                           and cod_med = :med
                           and num_lig = :numLig
                        """, nativeQuery = true)
        void deleteLigPhar(
                        @Param("soc") String soc,
                        @Param("mat") String mat,
                        @Param("fam") Integer fam,
                        @Param("datSoin") LocalDate datSoin,
                        @Param("med") String med,
                        @Param("numLig") Long numLig);
}