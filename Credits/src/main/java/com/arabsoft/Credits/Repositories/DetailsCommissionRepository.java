package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.CleDetailsCommission;
import com.arabsoft.Credits.Entities.DetailsCommission;
import com.arabsoft.Credits.Projections.DetailsCommissionProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetailsCommissionRepository extends JpaRepository<DetailsCommission, CleDetailsCommission> {

    @Query(value ="SELECT \n" +
            "    t.cod_soc,\n" +
            "    t.num_comm,\n" +
            "    t.mat_pers,\n" +
            "    t.num_dem_pret,\n" +
            "    t.resultat_comm,\n" +
            "    t.mnt_acc,\n" +
            "    t.dat_effet,\n" +
            "    t.nbr_ech_acc,\n" +
            "    t.delai_grace,\n" +
            "    t.nbr_tranche,\n" +
            "    t.cod_rejet,\n" +
            "    t.cod_etat_pret,\n" +
            "    t.typ_etat,\n" +
            "    t.obs_comm,\n" +
            "    dp.dat_dem,\n" +
            "    dp.typ_pret AS typPret,\n" +
            "    dp.cod_grp_pret AS codGrpPret,\n" +
            "    dp.mnt_dem AS mntDem,\n" +
            "    tp.lib_pret,\n" +
            "    p.nom_pers || ' ' || p.pren_pers AS nomPren\n" +
            "FROM \n" +
            "    detail_commission t\n" +
            "LEFT JOIN \n" +
            "    demande_pret dp\n" +
            "    ON t.cod_soc = dp.cod_soc\n" +
            "    AND t.num_dem_pret = dp.num_dem_pret\n" +
            "    AND t.mat_pers = dp.mat_pers\n" +
            "LEFT JOIN \n" +
            "    type_pret tp\n" +
            "    ON dp.cod_grp_pret = tp.cod_grp_pret\n" +
            "    AND dp.typ_pret = tp.typ_pret\n" +
            "    AND tp.cod_soc =:soc\n" +
            "LEFT JOIN \n" +
            "    personnel p\n" +
            "    ON t.cod_soc = p.cod_soc\n" +
            "    AND t.mat_pers = p.mat_pers\n" +
            "WHERE \n" +
            "    t.cod_soc =:soc and  t.num_comm=:num ",nativeQuery = true)
    List<DetailsCommissionProjection> getDetailComm(@Param("soc") String soc, @Param("num") Long num);
}
