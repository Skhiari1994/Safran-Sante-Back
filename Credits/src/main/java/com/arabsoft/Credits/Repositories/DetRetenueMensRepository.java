package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.CleDetRetenueMens;
import com.arabsoft.Credits.Entities.DetRetenueMens;
import com.arabsoft.Credits.Projections.DetRetenueMensProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DetRetenueMensRepository extends JpaRepository<DetRetenueMens, CleDetRetenueMens> {

    @Query(value = "select d.cod_soc,\n" +
            "       d.mois_retenue,\n" +
            "       d.mat_pers,\n" +
            "       d.abrv_fixe,\n" +
            "       d.cod_pret,\n" +
            "       d.l_pret,\n" +
            "       d.mnt_period,\n" +
            "       d.mnt_int,\n" +
            "      (select p.cod_grp_pret\n" +
            "from pret_pers p,type_pret t\n" +
            "where p.cod_grp_pret=t.cod_grp_pret\n" +
            "and p.typ_pret=t.typ_pret\n" +
            "and cod_pret=d.cod_pret\n" +
            "and mat_pers=d.mat_pers) cod_grp_pret, \n" +
            "(select p.typ_pret\n" +
            "from pret_pers p,type_pret t\n" +
            "where p.cod_grp_pret=t.cod_grp_pret\n" +
            "and p.typ_pret=t.typ_pret\n" +
            "and cod_pret=d.cod_pret\n" +
            "and mat_pers=d.mat_pers) typ_pret,\n" +
            "   (select t.lib_pret\n" +
            "from pret_pers p,type_pret t\n" +
            "where p.cod_grp_pret=t.cod_grp_pret\n" +
            "and p.typ_pret=t.typ_pret\n" +
            "and cod_pret=d.cod_pret\n" +
            "and mat_pers=d.mat_pers)lib_pret from det_retenue_mens d where cod_soc=:soc and mat_pers=:mat and mois_retenue=LAST_DAY(TO_DATE(:dat, 'dd/mm/yyyy'))  and abrv_fixe=:abrv", nativeQuery = true)
    List<DetRetenueMensProjection> getDetRetenueMens(@Param("soc") String soc, @Param("mat") String mat,
            @Param("dat") String dat, @Param("abrv") String abrv);

}
