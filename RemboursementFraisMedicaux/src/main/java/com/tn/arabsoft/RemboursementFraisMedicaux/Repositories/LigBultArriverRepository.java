package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigBultArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigBultAct;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigBultArriver;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigBultArriverProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LigBultArriverRepository extends JpaRepository<LigBultArriver, CleLigBultArriver> {

        @Query(value = "select t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.num_fam,\n" +
                        "       t.dat_soin,\n" +
                        "       t.abrv_act,\n" +
                        "       t.num_lig,\n" +
                        "       t.prf_typ,\n" +
                        "       t.prf_cod,\n" +
                        "       t.dat_act,\n" +
                        "       t.indice,\n" +
                        "       t.mnt_honor,\n" +
                        "       t.mnt_net,\n" +
                        "       t.mnt_remb,\n" +
                        "       t.obs,\n" +
                        "       t.obs_a,\n" +
                        "       t.nbr_piece,\n" +
                        "       t.nbr_vign,\n" +
                        "       t.nat_act,\n" +
                        "       t.mtt_acte,\n" +
                        "       t.taux_act,\n" +
                        "       t.plafonne,\n" +
                        "       t.plafond,\n" +
                        "       t.a_indice,\n" +
                        "       t.ctr_duree,\n" +
                        "       t.duree_act,\n" +
                        "       t.imput_plaf, (select lib_act from acte where t.abrv_act=abrv_act)lib_act\n" +
                        "  from lig_bult_arriver t where t.cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin", nativeQuery = true)
        List<LigBultArriverProjection> getLigBultArriver(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);

        @Modifying
        @Transactional
        @Query(value = "update bord_arriver set CLOT_BORD = 'O'\n" +
                        "where cod_soc = :soc \n" +
                        "and cod_assur = :cod_assur\n" +
                        "and cod_bord  = :cod_bord", nativeQuery = true)
        void updateBordArriver(@Param("soc") String soc, @Param("cod_assur") String cod_assur,
                        @Param("cod_bord") String cod_bord);

        @Modifying
        @Transactional
        @Query(value = "delete from lig_bult_arriver where cod_soc = :soc and mat_pers = :mat and num_fam = :numFam and dat_soin = to_date(:datSoin, 'dd/mm/yyyy') and num_lig = :numLig", nativeQuery = true)
        void deleteLigBultArriver(@Param("soc") String soc, @Param("mat") String mat, @Param("numFam") String numFam,
                        @Param("datSoin") String datSoin, @Param("numLig") String numLig);

        @Modifying
        @Transactional
        @Query(value = "DELETE FROM lig_bult_arriver WHERE cod_soc = :codSoc AND mat_pers = :matPers AND num_fam = :numFam AND dat_soin = to_date(:datSoin, 'dd/mm/yyyy')", nativeQuery = true)
        void deleteByBulletin(@Param("codSoc") String codSoc, @Param("matPers") String matPers,
                        @Param("numFam") String numFam, @Param("datSoin") String datSoin);
}
