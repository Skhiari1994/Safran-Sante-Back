package com.arabsoft.gestionconvention.Repositories;

import com.arabsoft.gestionconvention.Entities.Cle.CleLigOffDemandeConv;
import com.arabsoft.gestionconvention.Entities.Cle.CleOffDemandeConv;
import com.arabsoft.gestionconvention.Entities.LigOffDemandeConv;
import com.arabsoft.gestionconvention.Projections.LigOffDemandeConvProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface LigOffDemandeConvRepository extends JpaRepository<LigOffDemandeConv, CleLigOffDemandeConv> {

    @Query(value="SELECT t.cod_conv,\n" +
            "       t.cod_soc,\n" +
            "       t.mat_pers,\n" +
            "       t.cod_off,\n" +
            "       t.seq,\n" +
            "       t.num_tel,\n" +
            "       t.mnt_off,\n" +
            "       t.mod_p,\n" +
            "       t.num_retr,\n" +
            "       t.mois,\n" +
            "       t.etat_lig_off,\n" +
            "       t.obs_off,\n" +
            "       (select p.nom_pers || ' ' || p.pren_pers\n" +
            "          from personnel p\n" +
            "         where p.mat_pers = t.mat_pers) nom\n" +
            "FROM LIG_OFF_DEMANDE_CONV t\n" +
            "WHERE cod_soc =:soc\n" +
            "  AND to_char(t.mois, 'MM/YYYY') = to_char(to_date(:mois, 'MM/YYYY'), 'MM/YYYY')\n" +
            "  AND t.mat_pers IN (\n" +
            "        SELECT mat_pers\n" +
            "        FROM personnel\n" +
            "        WHERE corps = NVL(:corps, corps)\n" +
            "      )\n" +
            "  AND NVL(t.etat_lig_off, 'I') = 'I'\n" +
            "  AND (t.cod_conv, t.cod_soc, t.mat_pers, t.cod_off, t.seq) IN (\n" +
            "        SELECT cod_conv, cod_soc, mat_pers, cod_off, seq\n" +
            "        FROM off_demande_conv\n" +
            "        WHERE etat_off_dem = 'V' AND dat_susp IS NULL)\n" ,nativeQuery = true)
    List<LigOffDemandeConvProjection> getLigOffConv(@Param("soc")String soc, @Param("mois")String mois, @Param("corps")String corps);

    @Modifying
    @Transactional
    @Query(value="update lig_off_demande_conv set mod_p=:pay where cod_conv=:conv and cod_off=:off and mat_pers=:mat and seq=:seq",nativeQuery = true)
    void updateLigOffConvModPay(@Param("conv")String conv,@Param("off")String off,@Param("mat")String mat,
                          @Param("seq")String seq,@Param("pay")String pay);

    @Modifying
    @Transactional
    @Query(value="update lig_off_demande_conv set etat_lig_off=:etat where cod_conv=:conv and cod_off=:off and mat_pers=:mat and seq=:seq",nativeQuery = true)
    void updateLigOffConvEtat(@Param("conv")String conv,@Param("off")String off,@Param("mat")String mat,
                                @Param("seq")String seq,@Param("etat")String etat);
    @Modifying
    @Transactional
    @Query(value = "UPDATE lig_off_demande_conv " +
            "SET etat_lig_off = :etat " +
            "WHERE cod_conv = :conv AND cod_off = :off AND mat_pers = :mat " +
            "AND seq = :seq AND mois = :mois", nativeQuery = true)
    int updateLigOffConvEtatVal(@Param("conv") String conv,
                                @Param("off") String off,
                                @Param("mat") String mat,
                                @Param("seq") Long seq,
                                @Param("mois") LocalDate mois,
                                @Param("etat") String etat);
    @Modifying
    @Transactional
    @Query(value = "UPDATE lig_off_demande_conv " +
            "SET mod_p = :modP " +
            "WHERE cod_conv = :conv AND cod_off = :off AND mat_pers = :mat " +
            "AND seq = :seq AND mois = :mois", nativeQuery = true)
    int updateLigOffConvVir(@Param("conv") String conv,
                                @Param("off") String off,
                                @Param("mat") String mat,
                                @Param("seq") Long seq,
                                @Param("mois") LocalDate mois,
                                @Param("modP") String modP);
    @Query(value="select * from lig_off_demande_conv where cod_conv=:conv and cod_off=:off and mat_pers=:mat and seq=:seq  and mois=:mois",nativeQuery = true)
    LigOffDemandeConv getLigOffById(@Param("conv")String conv,@Param("off")String off,@Param("mat")String mat,
                                    @Param("seq")Long  seq,@Param("mois") LocalDate mois);
}
