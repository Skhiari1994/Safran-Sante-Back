package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.CleLigPret;
import com.arabsoft.Credits.Entities.LigPret;
import com.arabsoft.Credits.Projections.LigPretProjection;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LigPretRepository extends JpaRepository<LigPret, CleLigPret> {

    @Query(value="select l.cod_soc,\n" +
            "       l.mat_pers,\n" +
            "       l.cod_pret,\n" +
            "       l.l_pret,\n" +
            "       l.cod_typ_bul,\n" +
            "       l.mois_pret_prevu,\n" +
            "       l.mois_pret,\n" +
            "       l.mnt_period,\n" +
            "       l.mnt_int,\n" +
            "       l.int_grace,\n" +
            "       l.cap_rest,\n" +
            "       l.val_pret,\n" +
            "       l.reg_pret,\n" +
            "       l.nature_etat_pret,\n" +
            "       l.taux_int,\n" +
            "       l.num_retr from lig_pret l where cod_soc=:soc and mat_pers=:mat and l.cod_pret=:pret order by l.l_pret",nativeQuery = true)
    List<LigPret> getLigPRet(@Param("soc")String soc, @Param("mat")String mat, @Param("pret")String pret);
    @Query(value="select l.cod_soc,\n" +
            "       l.mat_pers,\n" +
            "       l.cod_pret,\n" +
            "       l.l_pret,\n" +
            "       l.cod_typ_bul,\n" +
            "       l.mois_pret_prevu,\n" +
            "       l.mois_pret,\n" +
            "       l.mnt_period,\n" +
            "       l.mnt_int,\n" +
            "       l.int_grace,\n" +
            "       l.cap_rest,\n" +
            "       l.val_pret,\n" +
            "       l.reg_pret,\n" +
            "       l.nature_etat_pret,\n" +
            "       l.taux_int,\n" +
            "       l.num_retr from lig_pret l where cod_soc=:soc and mat_pers=:mat and l.cod_pret=:pret and l.l_pret=:lpret",nativeQuery = true)
    LigPret getLigPretPers(@Param("soc")String soc, @Param("mat")String mat, @Param("pret")String pret,@Param("lpret")String lpret);
    @Query(value="select l.cod_soc,\n" +
            "       l.mat_pers,\n" +
            " (select nom_pers||' '||pren_pers from personnel where l.mat_pers=mat_pers)nom, \n "+
            "(select typ_pret \n" +
            "from pret_pers\n" +
            "where cod_soc =l.cod_soc\n" +
            "and mat_pers = l.mat_pers\n" +
            "and cod_pret = l.cod_pret)type, \n"+
            "       l.cod_pret,\n" +
            "       l.l_pret,\n" +
            "       l.cod_typ_bul,\n" +
            "       l.mois_pret_prevu,\n" +
            "       l.mois_pret,\n" +
            "       l.mnt_period,\n" +
            "       l.mnt_int,\n" +
            "       l.int_grace,\n" +
            "       l.cap_rest,\n" +
            "       l.val_pret,\n" +
            "       l.reg_pret,\n" +
            "       l.nature_etat_pret,\n" +
            "       l.taux_int,\n" +
            "       l.num_retr from lig_pret l \n" +
            " where cod_soc = :soc\n" +
            "   and to_char(mois_pret, 'mm/yyyy') = :mois \n" +
            "   and val_pret = 'O'\n" +
            "   and reg_pret = 'N'\n" +
            "   and (cod_soc, mat_pers, cod_pret) in\n" +
            "       (select cod_soc, mat_pers, cod_pret\n" +
            "          from pret_pers\n" +
            "         where cod_grp_pret = :grpPret)\n" +
            "   and (cod_soc, mat_pers) in\n" +
            "       (select cod_soc, mat_pers from personnel where corps = :corps)\n" +
            "   and (cod_soc, mat_pers, cod_pret) in\n" +
            "       (select cod_soc, mat_pers, cod_pret\n" +
            "          from pret_pers\n" +
            "         where to_char(prt_dat_deb, 'mm/yyyy') =\n" +
            "               decode(:filtr,\n" +
            "                      'G',\n" +
            "                      to_char(prt_dat_deb, 'mm/yyyy'),\n" +
            "                      to_char(:mois, 'mm/yyyy')))\n",nativeQuery = true)
    List<LigPretProjection> getLigRegManuelle(@Param("soc")String soc, @Param("mois")String mois, @Param("filtr")String filtr,@Param("corps")String corps, @Param("grpPret") String grpPret);


    @Modifying
    @Transactional
    @Query(value = "UPDATE lig_pret SET reg_pret = :regPret " +
            "WHERE cod_soc = :codSoc AND mat_pers = :matPers AND cod_pret = :codPret AND l_pret = :lPret", nativeQuery = true)
    void updateRegPret(@Param("codSoc") String codSoc,
                       @Param("matPers") String matPers,
                       @Param("codPret") BigDecimal codPret,
                       @Param("lPret") BigDecimal lPret,
                       @Param("regPret") String regPret);
}
