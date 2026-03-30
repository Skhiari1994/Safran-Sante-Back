package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.CleRetenuMensuel;
import com.arabsoft.Credits.Entities.RetenuMensuel;
import com.arabsoft.Credits.Projections.RetenuMensuelProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

public interface RetenuMensuelRepository extends JpaRepository<RetenuMensuel, CleRetenuMensuel> {

        @Query(value = "SELECT d.cod_soc,\n" +
                        "       d.mois_retenue,\n" +
                        "       d.mat_pers,\n" +
                        "       d.abrv_fixe,\n" +
                        "       d.mnt_retenue,\n" +
                        "       d.valid,\n" +
                        "       d.date_fin,\n" +
                        "       d.motif,\n" +
                        "       (select p.pren_pers ||' '||p.nom_pers from personnel p where p.mat_pers=d.mat_pers)nom \n"
                        +
                        "FROM RETENU_MENSUEL d \n" +
                        "WHERE d.COD_SOC = :soc \n" +
                        "  AND d.MOIS_RETENUE = LAST_DAY(TO_DATE(:dat, 'mm/yyyy'))", nativeQuery = true)
        List<RetenuMensuelProjection> getRetenuMens(@Param("soc") String soc, @Param("dat") String dat);

        @Query(value = "SELECT d.cod_soc,\n" +
                        "       d.mois_retenue,\n" +
                        "       d.mat_pers,\n" +
                        "       d.abrv_fixe,\n" +
                        "       d.mnt_retenue,\n" +
                        "       d.valid,\n" +
                        "       d.date_fin,\n" +
                        "       d.motif,\n" +
                        "       (select p.pren_pers ||' '||p.nom_pers from personnel p where p.mat_pers=d.mat_pers)nom \n"
                        +
                        "FROM RETENU_MENSUEL d\n" +
                        "WHERE d.COD_SOC = :soc\n" +
                        "  AND valid='N'\n" +
                        "  AND d.MOIS_RETENUE = :lastDayOfMonth \n", nativeQuery = true)
        List<RetenuMensuelProjection> getRetenuMensValdDate(@Param("soc") String soc,
                        @Param("lastDayOfMonth") Date lastDayOfMonth);

        @Query(value = "SELECT d.cod_soc,\n" +
                        "       d.mois_retenue,\n" +
                        "       d.mat_pers,\n" +
                        "       d.abrv_fixe,\n" +
                        "       d.mnt_retenue,\n" +
                        "       d.valid,\n" +
                        "       d.date_fin,\n" +
                        "       d.motif,\n" +
                        "       (select p.pren_pers ||' '||p.nom_pers from personnel p where p.mat_pers=d.mat_pers)nom \n"
                        +
                        "FROM RETENU_MENSUEL d \n" +
                        "WHERE d.COD_SOC = :soc \n" +
                        " and valid='O'  AND d.MOIS_RETENUE = LAST_DAY(TO_DATE(:dat, 'mm/yyyy'))", nativeQuery = true)
        List<RetenuMensuelProjection> getRetenuMensValdO(@Param("soc") String soc, @Param("dat") String dat);

        @Modifying
        @Transactional
        @Query(value = "update RETENU_MENSUEL  set valid='O'\n" +
                        "where mois_retenue=LAST_DAY(TO_DATE(:dat, 'mm/yyyy'))", nativeQuery = true)
        void validRetMens(@Param("dat") String dat);

        @Query(value = "SELECT d.cod_soc,\n" +
                        "       d.mois_retenue,\n" +
                        "       d.mat_pers,\n" +
                        "       d.abrv_fixe,\n" +
                        "       d.mnt_retenue,\n" +
                        "       d.valid,\n" +
                        "       d.date_fin,\n" +
                        "       d.motif,\n" +
                        "       (select p.pren_pers ||' '||p.nom_pers from personnel p where p.mat_pers=d.mat_pers)nom \n"
                        +
                        "FROM RETENU_MENSUEL d \n" +
                        "WHERE d.COD_SOC = :soc \n" +
                        "  AND d.MOIS_RETENUE = LAST_DAY(TO_DATE(:dat, 'mm/yyyy'))\n" +
                        "  AND EXISTS (SELECT 1 FROM personnel p WHERE p.mat_pers = d.mat_pers AND p.typ_aff = :type)", nativeQuery = true)
        List<RetenuMensuelProjection> getRetenuMensByType(@Param("soc") String soc, @Param("dat") String dat,
                        @Param("type") String type);
}
