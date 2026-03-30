package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigBultMed;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigBultAct;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigBultMed;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigBultMedProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LigBultMedRepository extends JpaRepository<LigBultMed, CleLigBultMed> {

    @Query(value="select nvl(max(nvl(num_lig_med,0)),0) + 1  \n" +
            "from lig_bult_med\n" +
            "where COD_SOC = :soc \n" +
            "and MAT_PERS = :mat \n" +
            "and NUM_FAM = :numFam \n" +
            "and DAT_SOIN = :datSoin \n" +
            "and COD_MED = :codMed",nativeQuery = true)
    Long getNumLigMed(@Param("soc")String soc, @Param("mat")String mat, @Param("numFam")Long numFam, @Param("datSoin") LocalDate datSoin, @Param("codMed")String codMed);

    @Query(value="select * from lig_bult_med where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin",nativeQuery = true)
    List<LigBultMed> getLigBultMed(@Param("soc")String soc, @Param("mat")String mat, @Param("numFam")String numFam, @Param("datSoin")String datSoin);

    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_fam,\n" +
            "       t.dat_soin,\n" +
            "       t.abrv_act,\n" +
            "       t.cod_med,\n" +
            "       t.num_lig,\n" +
            "       t.indice,\n" +
            "       t.mnt_honor,\n" +
            "       t.mnt_net,\n" +
            "       t.mnt_remb,\n" +
            "       t.accord_med,\n" +
            "       t.dat_act,\n" +
            "       t.prf_typ,\n" +
            "       t.prf_cod,\n" +
            "       t.num_pec_med,\n" +
            "       t.mdc_prix,\n" +
            "       t.med_prix,\n" +
            "       t.prix_remb,\n" +
            "       t.num_lig_med,\n" +
            "       t.mut_mnt_net,\n" +
            "       t.nbr_j,\n" +
            "       (select r.lib_med from ref_med r where r.cod_med=t.cod_med)lib_med," +
            "       (select ETAB_RSOC ||' '|| PR_RSOC  from ref_etablis e where e.prf_typ=t.prf_typ and e.prf_cod=t.prf_cod)lib_etablis  from lig_bult_med t where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin",nativeQuery = true)
    List<LigBultMedProjection> getLigBultMedCons(@Param("soc")String soc, @Param("mat")String mat, @Param("numFam")String numFam, @Param("datSoin")String datSoin);
    @Modifying
    @Transactional
    @Query(value="delete from lig_bult_med where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and abrv_act=:abrv and num_lig_med=:numLig and cod_med=:codMed",nativeQuery = true)
    void deleteLigBultMed(@Param("soc")String soc,@Param("mat")String mat,@Param("fam")String fam,
                          @Param("datSoin")String datSoin,@Param("abrv")String abrv,@Param("numLig")String numLig,@Param("codMed")String codMed);
}
