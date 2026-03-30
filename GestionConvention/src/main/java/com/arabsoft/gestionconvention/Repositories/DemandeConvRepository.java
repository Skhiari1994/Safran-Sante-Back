package com.arabsoft.gestionconvention.Repositories;

import com.arabsoft.gestionconvention.Entities.Cle.CleDemandeConv;
import com.arabsoft.gestionconvention.Entities.DemandeConv;
import com.arabsoft.gestionconvention.Projections.ConvProjection;
import com.arabsoft.gestionconvention.Projections.DemondeConvProjection;
import com.arabsoft.gestionconvention.Projections.SuspConvProjection;
import com.arabsoft.gestionconvention.Projections.NaisProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DemandeConvRepository extends JpaRepository<DemandeConv, CleDemandeConv> {

        @Query(value = "select nom_pers || ' ' || pren_pers nom_pren,\n" +
                        "       \n" +
                        "       corps,\n" +
                        "       p.cod_affect,\n" +
                        "       (select lib_affect\n" +
                        "          from affectation af\n" +
                        "         where p.cod_affect = af.cod_affect) lib_affect,\n" +
                        "      p.cod_lieu_geog,\n" +
                        "       (select lib_LIEU\n" +
                        "          from PRM_LIEU_GEOGRAPHIQUE lg\n" +
                        "         where p.COD_LIEU_GEOG = lg.COD_LIEU_GEOG) lib_lieu,\n" +
                        "       p.cod_typ_depart,\n" +
                        "       (select lib_typ_depart\n" +
                        "          from type_depart td\n" +
                        "         where td.cod_typ_depart = p.cod_typ_depart) lib_depart,\n" +
                        "     \n" +
                        "       \n" +
                        "       p.cin\n" +
                        "       ,p.num_retr,\n" +
                        "       p.mat_pers,\n" +
                        "       p.mat_int,\n" +
                        "       p.dat_nais\n" +
                        "      \n" +
                        "\n" +
                        "  from personnel p\n" +
                        "  \n" +
                        "  where p.cod_soc=:codSoc and etat_act='A'", nativeQuery = true)
        List<DemondeConvProjection> getDemandeConv(@Param("codSoc") String codSoc);

        @Query(value = "select t.cod_conv,\n" +
                        "       t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.dat_dem_conv,\n" +
                        "       t.etat_dem,\n" +
                        "       t.dat_saisie,\n" +
                        "       t.cod_user,\n" +
                        "       t.dat_min_fin,\n" +
                        "       t.obs,\n" +
                        "       t.cod_lieu_geog,\n" +
                        "       t.cod_typ_depart,\n" +
                        "       t.cod_affect,\n" +
                        "       t.dat_susp,\n" +
                        "       t.cin,\n" +
                        "       (select lib_conv from convention c where c.cod_conv = t.cod_conv) lib_conv,\n" +
                        "       \n" +
                        "       p.nom_pers || ' ' || p.pren_pers nom_pren,\n" +
                        "       p.num_retr,\n" +
                        "       p.mat_pers,\n" +
                        "       p.mat_int,\n" +
                        "       p.dat_nais,\n" +
                        "       p.cin\n" +
                        "  from demande_conv t, personnel p\n" +
                        " where t.cod_soc = '01'\n" +
                        "   and p.cod_soc = t.cod_soc\n" +
                        "   and t.mat_pers = p.mat_pers\n" +
                        "   and t.ETAT_DEM = 'I'", nativeQuery = true)
        List<DemondeConvProjection> getPersonnel();

        @Query(value = " select  p.dat_nais,corps\n" +
                        "    from personnel p\n" +
                        "    where p.cod_soc = :codSoc\n" +
                        "    and  p.mat_pers =:matPers", nativeQuery = true)
        List<NaisProjection> getNais(@Param("codSoc") String codSoc, @Param("matPers") String matPers);

        @Query(value = "select t.cod_conv,\n" +
                        "       t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.dat_dem_conv,\n" +
                        "       t.etat_dem,\n" +
                        "       t.dat_saisie,\n" +
                        "       t.cod_user,\n" +
                        "       t.dat_min_fin,\n" +
                        "       t.obs,\n" +
                        "       t.cod_lieu_geog,\n" +
                        "       t.cod_typ_depart,\n" +
                        "       t.cod_affect,\n" +
                        "       t.dat_susp,\n" +
                        "       t.cin,\n" +
                        "       (select lib_conv from convention c where c.cod_conv = t.cod_conv) lib_conv,\n" +
                        "       (select p.nom_pers || ' ' || p.pren_pers\n" +
                        "          from personnel p\n" +
                        "         where p.mat_pers = t.mat_pers) nom\n" +
                        "  from demande_conv t\n" +
                        " where cod_soc = :soc\n" +
                        "   and ETAT_DEM = 'V'", nativeQuery = true)
        List<SuspConvProjection> getConvSusp(@Param("soc") String soc);

        @Query(value = "select t.cod_conv,\n" +
                        "       t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.dat_dem_conv,\n" +
                        "       t.etat_dem,\n" +
                        "       t.dat_saisie,\n" +
                        "       t.cod_user,\n" +
                        "       t.dat_min_fin,\n" +
                        "       t.obs,\n" +
                        "       t.cod_lieu_geog,\n" +
                        "       t.cod_typ_depart,\n" +
                        "       t.cod_affect,\n" +
                        "       t.dat_susp,\n" +
                        "       t.cin,\n" +
                        "       (select lib_conv from convention c where c.cod_conv = t.cod_conv) lib_conv,\n" +
                        "       (select p.nom_pers || ' ' || p.pren_pers\n" +
                        "          from personnel p\n" +
                        "         where p.mat_pers = t.mat_pers) nom\n" +
                        "  from demande_conv t\n" +
                        " where cod_soc = :soc\n" +
                        "   and ETAT_DEM = 'I'   " +
                        "and t.mat_pers= :mat", nativeQuery = true)
        List<ConvProjection> getConvVal(@Param("soc") String soc, @Param("mat") String mat);

        @Query(value = "select t.cod_conv,\n" +
                        "       t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.dat_dem_conv,\n" +
                        "       t.etat_dem,\n" +
                        "       t.dat_saisie,\n" +
                        "       t.cod_user,\n" +
                        "       t.dat_min_fin,\n" +
                        "       t.obs,\n" +
                        "       t.cod_lieu_geog,\n" +
                        "       t.cod_typ_depart,\n" +
                        "       t.cod_affect,\n" +
                        "       t.dat_susp,\n" +
                        "       t.cin,\n" +
                        "       (select lib_conv from convention c where c.cod_conv = t.cod_conv) lib_conv,\n" +
                        "       (select p.nom_pers || ' ' || p.pren_pers\n" +
                        "          from personnel p\n" +
                        "         where p.mat_pers = t.mat_pers) nom\n" +
                        "  from demande_conv t\n" +
                        " where cod_soc = :soc\n" +
                        "and t.mat_pers= :mat", nativeQuery = true)
        List<ConvProjection> getConvValIn(@Param("soc") String soc, @Param("mat") String mat);

        @Query(value = "select t.cod_conv,\n" +
                        "       t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.dat_dem_conv,\n" +
                        "       t.etat_dem,\n" +
                        "       t.dat_saisie,\n" +
                        "       t.cod_user,\n" +
                        "       t.dat_min_fin,\n" +
                        "       t.obs,\n" +
                        "       t.cod_lieu_geog,\n" +
                        "       t.cod_typ_depart,\n" +
                        "       t.cod_affect,\n" +
                        "       t.dat_susp,\n" +
                        "       t.cin,\n" +
                        "       (select lib_conv from convention c where c.cod_conv = t.cod_conv) lib_conv,\n" +
                        "       (select p.nom_pers || ' ' || p.pren_pers\n" +
                        "          from personnel p\n" +
                        "         where p.mat_pers = t.mat_pers) nom\n" +
                        "  from demande_conv t\n" +
                        " where cod_soc = :soc\n" +
                        " and cod_conv=:codConv\n" +
                        "and t.mat_pers= :mat", nativeQuery = true)
        List<ConvProjection> getConvValInCodConv(@Param("soc") String soc, @Param("mat") String mat,
                        @Param("codConv") String codConv);

        @Query(value = "select t.cod_conv,\n" +
                        "       t.cod_soc,\n" +
                        "       t.mat_pers,\n" +
                        "       t.dat_dem_conv,\n" +
                        "       t.etat_dem,\n" +
                        "       t.dat_saisie,\n" +
                        "       t.cod_user,\n" +
                        "       t.dat_min_fin,\n" +
                        "       t.obs,\n" +
                        "       t.cod_lieu_geog,\n" +
                        "       t.cod_typ_depart,\n" +
                        "       t.cod_affect,\n" +
                        "       t.dat_susp,\n" +
                        "       t.cin,\n" +
                        "       (select lib_conv from convention c where c.cod_conv = t.cod_conv) lib_conv,\n" +
                        "       (select p.nom_pers || ' ' || p.pren_pers\n" +
                        "          from personnel p\n" +
                        "         where p.mat_pers = t.mat_pers) nom\n" +
                        "  from demande_conv t\n" +
                        " where cod_soc = :soc\n" +

                        "and t.etat_dem= 'V'", nativeQuery = true)
        List<ConvProjection> getConvValSusp(@Param("soc") String soc);

        @Query(value = "SELECT GET_DAT_RETRAITE(:soc, :mat) FROM dual", nativeQuery = true)
        String getDateRetraite(
                        @Param("soc") String codSoc,
                        @Param("mat") String matPers);
}
