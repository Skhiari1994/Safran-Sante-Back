package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.CleLigPhar;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigBult;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.LigPhar;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigPharProjection;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.LigPharProjectionCons;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LigPharRepository extends JpaRepository<LigPhar, CleLigPhar> {
    @Query(value="select r.LIB_MED,COD_MED,MDC_PRIX,MED_PRIX,PRIX_REMB,r.abrv_act\n" +
            "from ref_med r ,acte a,bareme_remb b \n" +
            "where r.ABRV_ACT = a.abrv_act\n" +
            "and b.abrv_act = a.abrv_act\n" +
            "and b.cod_fil =:codFil \n" +
            "and b.COD_ASSUR =:codAssur",nativeQuery = true)
    List<LigPharProjection> getMed(@Param("codFil")String codFil, @Param("codAssur") String codAssur);

    @Query(value="select * from lig_phar where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin",nativeQuery = true)
    List<LigPhar> getLigPhar(@Param("soc")String soc, @Param("mat")String mat, @Param("numFam")String numFam, @Param("datSoin")String datSoin);

    @Query(value="select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_fam,\n" +
            "       t.dat_soin,\n" +
            "       t.cod_med,\n" +
            "       t.num_lig,\n" +
            "       t.indice,\n" +
            "       t.dat_act,\n" +
            "       t.prf_typ,\n" +
            "       t.prf_cod,\n" +
            "       t.mnt_honor,\n" +
            "       t.mnt_net,\n" +
            "       t.mnt_remb,\n" +
            "       t.mdc_prix,\n" +
            "       t.med_prix,\n" +
            "       t.prix_remb,\n" +
            "       t.obs,\n" +
            "       t.obs_a,\n" +
            "       t.nbr_piece,\n" +
            "       t.nbr_vign,\n" +
            "       t.abrv_act,\n" +
            "       (select lib_med from ref_med r where r.cod_med=t.cod_med)lib_med,\n" +
            "       (select e.etab_rsoc from etabliss e where e.prf_typ=t.prf_typ and e.prf_cod=t.prf_cod)lib_etablis\n" +
            "        from lig_phar t where cod_soc=:soc and mat_pers=:mat and num_fam=:numFam and dat_soin=:datSoin",nativeQuery = true)
    List<LigPharProjectionCons> getLigPharCons(@Param("soc")String soc, @Param("mat")String mat, @Param("numFam")String numFam, @Param("datSoin")String datSoin);
    @Modifying
    @Transactional
    @Query(value="delete from lig_phar where   cod_soc=:soc and mat_pers=:mat and num_fam=:fam and dat_soin=:datSoin and cod_med=:med and num_lig=:numLig",nativeQuery = true)
    void deleteLigPhar(@Param("soc")String soc,@Param("mat")String mat,@Param("fam")String fam,@Param("datSoin")String datSoin,@Param("med")String med,@Param("numLig")String numLig);
}
