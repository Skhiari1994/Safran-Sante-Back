package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.ClePriseCharge;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.PriseCharge;
import com.tn.arabsoft.RemboursementFraisMedicaux.Projections.PriseChargeProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PriseChargeRepository extends JpaRepository<PriseCharge, ClePriseCharge> {

    @Query(value="\n" +
            "select t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.num_pec,\n" +
            "       t.dat_pec,\n" +
            "       t.num_fam,\n" +
            "       t.etat_pec,\n" +
            "       t.prf_typ,\n" +
            "       t.prf_cod,\n" +
            "       t.mnt_pec,\n" +
            "       t.mnt_remb,\n" +
            "       t.dat_eff,\n" +
            "       CASE\n" +
            "         WHEN t.num_fam <> 0 THEN\n" +
            "          (SELECT nom_pren\n" +
            "             FROM famille\n" +
            "            WHERE cod_soc = t.cod_soc\n" +
            "              AND mat_pers = t.mat_pers\n" +
            "              AND num_fam = t.num_fam)\n" +
            "         ELSE\n" +
            "          'Adhérent'\n" +
            "       END AS nom,\n" +
            "       (select ETAB_RSOC || ' ' || PR_RSOC\n" +
            "          from ref_etablis\n" +
            "         where PRF_TYP = t.prf_typ\n" +
            "           and PRF_COD = t.PRF_COD) nomEtab\n" +
            "  from prise_charge t where t.cod_soc=:soc and t.mat_pers=:mat ORDER BY t.dat_pec DESC\n",nativeQuery = true)
    List<PriseChargeProjection> getPriseCharge(@Param("soc")String soc, @Param("mat")String mat);

    @Modifying
    @Transactional
    @Query(value="delete from prise_charge where   cod_soc=:soc and mat_pers=:mat and num_pec=:pec",nativeQuery = true)
    void deletePriseCharge(@Param("soc")String soc,@Param("mat")String mat,@Param("pec")String pec);
}
