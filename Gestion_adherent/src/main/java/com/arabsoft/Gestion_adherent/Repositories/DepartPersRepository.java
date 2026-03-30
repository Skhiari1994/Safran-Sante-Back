package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.Cle.CleDepartPers;
import com.arabsoft.Gestion_adherent.Entities.Depart_Pers;
import com.arabsoft.Gestion_adherent.Projections.DepartPersProjection;
import com.arabsoft.Gestion_adherent.Projections.ListPersonnelDepartProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DepartPersRepository extends JpaRepository<Depart_Pers, CleDepartPers> {

    @Query(value="select d.cod_soc,\n" +
            "       d.mat_pers,\n" +
            "(select  PERSONNEL.PREN_PERS || ' ' || PERSONNEL.NOM_PERS NOM_PREN from personnel where mat_pers=d.mat_pers)nomPers,\n"+
            "       d.cod_typ_depart,\n" +
            "       (select lib_typ_depart\n" +
            "          from type_depart\n" +
            "         where cod_typ_depart = d.cod_typ_depart) libDepart,\n" +
            "       d.dat_depart,\n" +
            "       d.dat_sais_depart,\n" +
            "       d.obs_depart,\n" +
            "       d.etat_depart,\n" +
            "       d.corps,\n" +
            "       (select lib_corps from corps where cod_corps=d.corps)libCorps,\n" +
            "       d.cod_affect,\n" +
            "       (select lib_affect from affectation where cod_affect = d.cod_affect) libAffect,\n" +
            "       d.cod_lieu_geog,\n" +
            "       (select lib_lieu\n" +
            "          from PRM_LIEU_GEOGRAPHIQUE\n" +
            "         where cod_lieu_geog = d.cod_lieu_geog) libLieu,\n" +
             "      (select dat_nais from personnel where mat_pers=d.mat_pers)datNais,\n" +
            "       (select dat_emb from personnel where mat_pers=d.mat_pers)datEmb" +
            "  from depart_pers d\n" +
            " where cod_soc =:soc\n" +
            "   and nvl(cod_typ_depart, 'ZZ') <> '10'\n",nativeQuery = true)
    List<DepartPersProjection> getListDepart(@Param("soc")String soc);
    @Query(value="select d.cod_soc,\n" +
            "       d.mat_pers,\n" +
            "(select  PERSONNEL.PREN_PERS || ' ' || PERSONNEL.NOM_PERS NOM_PREN from personnel where mat_pers=d.mat_pers)nomPers,\n"+
            "       d.cod_typ_depart,\n" +
            "       (select lib_typ_depart\n" +
            "          from type_depart\n" +
            "         where cod_typ_depart = d.cod_typ_depart) libDepart,\n" +
            "       d.dat_depart,\n" +
            "       d.dat_sais_depart,\n" +
            "       d.obs_depart," +
            "       d.etat_depart,\n" +
            "       d.corps,\n" +
            "       (select lib_corps from corps where cod_corps=d.corps)libCorps,\n" +
            "       d.cod_affect,\n" +
            "       (select lib_affect from affectation where cod_affect = d.cod_affect) libAffect,\n" +
            "       d.cod_lieu_geog,\n" +
            "       (select lib_lieu\n" +
            "          from PRM_LIEU_GEOGRAPHIQUE\n" +
            "         where cod_lieu_geog = d.cod_lieu_geog) libLieu,\n" +
            "      (select dat_nais from personnel where mat_pers=d.mat_pers)datNais,\n" +
            "       (select dat_emb from personnel where mat_pers=d.mat_pers)datEmb" +
            "  from depart_pers d\n" +
            " where cod_soc =:soc\n" +
            "   and cod_typ_depart = '10'\n",nativeQuery = true)
    List<DepartPersProjection> getListReinteg(@Param("soc")String soc);

    @Query(value="select d.cod_soc,\n" +
            "       d.mat_pers,\n" +
            "(select  PERSONNEL.PREN_PERS || ' ' || PERSONNEL.NOM_PERS NOM_PREN from personnel where mat_pers=d.mat_pers)nomPers,\n"+
            "       d.cod_typ_depart,\n" +
            "       (select lib_typ_depart\n" +
            "          from type_depart\n" +
            "         where cod_typ_depart = d.cod_typ_depart) libDepart,\n" +
            "       d.dat_depart,\n" +
            "       d.dat_sais_depart,\n" +
            "       d.obs_depart," +
            "       d.etat_depart,\n" +
            "       d.corps,\n" +
            "       (select lib_corps from corps where cod_corps=d.corps)libCorps,\n" +
            "       d.cod_affect,\n" +
            "       (select lib_affect from affectation where cod_affect = d.cod_affect) libAffect,\n" +
            "       d.cod_lieu_geog,\n" +
            "       (select lib_lieu\n" +
            "          from PRM_LIEU_GEOGRAPHIQUE\n" +
            "         where cod_lieu_geog = d.cod_lieu_geog) libLieu,\n" +
            "      (select dat_nais from personnel where mat_pers=d.mat_pers)datNais,\n" +
            "       (select dat_emb from personnel where mat_pers=d.mat_pers)datEmb" +
            "  from depart_pers d\n" +
            " where cod_soc =:soc\n" +
            "   and  nvl(etat_depart,'I' )='I' \n",nativeQuery = true)
    List<DepartPersProjection> getListDepartReintegInstance(@Param("soc")String soc);
    @Query(value="SELECT ALL PERSONNEL.MAT_PERS,\n" +
            "           PERSONNEL.PREN_PERS || ' ' || PERSONNEL.NOM_PERS nomPren,\n" +
            "           cod_affect, \n" +
            "           (select lib_affect\n" +
            "              from affectation\n" +
            "             where cod_affect = personnel.cod_affect) libAffect,\n" +
            "           cod_lieu_geog,\n" +
            "           (select lib_lieu\n" +
            "              from PRM_LIEU_GEOGRAPHIQUE\n" +
            "            where cod_lieu_geog = PERSONNEL.cod_lieu_geog) lib_lieu,\n" +
            "           corps,\n" +
            "           dat_nais,\n" +
            "           dat_emb\n" +
            "  FROM PERSONNEL\n" +
            " where cod_soc =:soc\n" +
            "   and etat_act = 'A'\n" +
            "   and COD_TYP_DEPART is null\n" +
            " order by to_number(mat_pers)\n",nativeQuery = true)
    List<ListPersonnelDepartProjection> getListPersonnelDepart(@Param("soc")String soc);

    @Query(value="SELECT ALL PERSONNEL.MAT_PERS,\n" +
            "           PERSONNEL.PREN_PERS || ' ' || PERSONNEL.NOM_PERS nomPren,\n" +
            "           cod_affect, \n" +
            "           (select lib_affect\n" +
            "              from affectation\n" +
            "             where cod_affect = personnel.cod_affect) libAffect,\n" +
            "           cod_lieu_geog,\n" +
            "           (select lib_lieu\n" +
            "              from PRM_LIEU_GEOGRAPHIQUE\n" +
            "            where cod_lieu_geog = PERSONNEL.cod_lieu_geog) lib_lieu,\n" +
            "           corps,\n" +
            "           dat_nais,\n" +
            "           dat_emb,cod_typ_depart,\n" +
            "       (select lib_typ_depart\n" +
            "          from type_depart\n" +
            "         where cod_typ_depart = PERSONNEL.cod_typ_depart) libDepart,dat_depart\n" +
            "  FROM PERSONNEL\n" +
            " where cod_soc =:soc\n" +
            "   and etat_act = 'B'\n" +
             " order by to_number(mat_pers)\n",nativeQuery = true)
    List<ListPersonnelDepartProjection> getListPersonnelReinteg(@Param("soc")String soc);
}
