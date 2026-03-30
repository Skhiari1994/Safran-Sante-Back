package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.Cles.ClePersonnel;
import com.arabsoft.Credits.Entities.Personnel;
import com.arabsoft.Credits.Projections.PersProjection;
import com.arabsoft.Credits.Projections.PersonnelAnticip;
import com.arabsoft.Credits.Projections.PersonnelPrejection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

   @Query(value=" SELECT DISTINCT \n" +
           "       p.num_retr,\n" +
           "       p.mat_pers,\n" +
           "       p.nom_pers || ' ' || p.pren_pers AS nom,\n" +
           "       p.dat_emb,\n" +
           "       get_dat_retraite( p.cod_soc, p.mat_pers)datRetraite,\n" +
           "       p.cod_sit,\n" +
           "       p.nbr_enf,\n" +
           "       p.dat_nais AS date_naissance,\n" +
           "       p.cod_lieu_geog,\n" +
           "       l.lib_lieu,\n" +
           "       p.cod_typ_depart,\n" +
           "       t.lib_typ_depart,\n" +
           "       p.cod_affect,\n" +
           "       a.lib_affect,\n" +
           "       p.corps\n," +
           " p.pers_carte \n"+
           "FROM   personnel p\n" +
           "       LEFT JOIN prm_lieu_geographique l ON p.cod_lieu_geog = l.cod_lieu_geog\n" +
           "       LEFT JOIN type_depart t ON p.cod_typ_depart = t.cod_typ_depart\n" +
           "       LEFT JOIN affectation a ON p.cod_affect = a.cod_affect\n" +
           "       JOIN position_pret pos ON p.cod_soc = pos.cod_soc AND p.cod_affect = pos.cod_motif\n" +
           "WHERE  p.cod_soc =:soc\n" +
           "       AND p.etat_act = 'A'\n" +
           "ORDER BY p.mat_pers\n",nativeQuery = true)
    List<PersonnelPrejection> getPersonnelCredit(@Param("soc") String soc);
    @Query(value=" SELECT DISTINCT \n" +
            "       p.num_retr,\n" +
            "       p.mat_pers,\n" +
            "       p.nom_pers || ' ' || p.pren_pers AS nom,\n" +
            "       p.dat_emb,\n" +
            "       get_dat_retraite( p.cod_soc, p.mat_pers)datRetraite,\n" +
            "       p.cod_sit,\n" +
            "       p.nbr_enf,\n" +
            "       p.dat_nais AS date_naissance,\n" +
            "       p.cod_lieu_geog,\n" +
            "       l.lib_lieu,\n" +
            "       p.cod_typ_depart,\n" +
            "       t.lib_typ_depart,\n" +
            "       p.cod_affect,\n" +
            "       a.lib_affect,\n" +
            "       p.corps\n," +
            " p.pers_carte \n"+
            "FROM   personnel p\n" +
            "       LEFT JOIN prm_lieu_geographique l ON p.cod_lieu_geog = l.cod_lieu_geog\n" +
            "       LEFT JOIN type_depart t ON p.cod_typ_depart = t.cod_typ_depart\n" +
            "       LEFT JOIN affectation a ON p.cod_affect = a.cod_affect\n" +
            "       JOIN position_pret pos ON p.cod_soc = pos.cod_soc AND p.cod_affect = pos.cod_motif\n" +
            "WHERE  p.cod_soc =:soc\n" +
            "       AND p.mat_pers =:mat\n" +
            "       AND p.etat_act = 'A'\n" +
            "ORDER BY p.mat_pers\n",nativeQuery = true)
    PersonnelPrejection getPersonnelCreditByMat(@Param("soc") String soc,@Param("mat") String mat);

    @Query(value="select distinct p.mat_pers,nom_pers||' '||pren_pers nom,dat_nais\n" +
            "from personnel p,pret_pers t\n" +
            "where \n" +
            "p.cod_soc  =:soc  and\n" +
            "p.cod_soc  = t.cod_soc  and\n" +
            "p.mat_pers = t.mat_pers and\n" +
            "cod_etat_pret ='D' \n" +
            "order by p.mat_pers",nativeQuery = true)
    List<PersonnelAnticip> getPersonnelsAnticipe(@Param("soc")String soc);

 @Query(value="select distinct p.mat_pers,nom_pers||' '||pren_pers nom,dat_nais\n" +
         "from personnel p,pret_pers t\n" +
         "where \n" +
         "p.cod_soc  =:soc  and\n" +
         "p.cod_soc  = t.cod_soc  and\n" +
         "p.mat_pers = t.mat_pers and\n" +
         "cod_etat_pret ='D' and t.mat_pers=:mat \n" +
         "order by p.mat_pers",nativeQuery = true)
PersonnelAnticip getPersonnelsAnticipeByMat(@Param("soc")String soc,@Param("mat")String mat);

 @Query(value="select p.cod_soc,p.mat_pers, p.cod_assur, p.nom_pers,p.sexe,p.cin,p.dat_nais,p.pren_pers,\n" +
         "       p.dat_emb, p.cod_sit,p.nbr_enf, p.cod_retr,p.num_retr,p.num_assur,p.dat_ass,p.cod_pay,\n" +
         "       p.rib, p.nom_pers_a,p.pren_pers_a,p.cod_natp,p.cod_banq,p.cod_agc,p.dat_dece,p.etat_act,\n" +
         "       p.dat_motif, p.cod_lieu_geog, p.bas_plafond, p.nom_jf,p.nom_jf_a,p.photo_pers, p.lieu_nais,\n" +
         "       p.mnt_param,  p.etat_prof,p.dat_aff_cnam,p.corps, p.cod_affect, p.dat_affect,p.cod_typ_depart,\n" +
         "       p.dat_depart,p.typ_aff,p.cod_user,p.dat_maj, p.mat_int,p.pers_carte,(p.pren_pers ||' '||p.nom_pers)full_name,(select lib_lieu from PRM_LIEU_GEOGRAPHIQUE where cod_lieu_geog=p.cod_lieu_geog)libLieu\n" +
         "  from personnel p  ",nativeQuery = true)
 List<PersProjection> getPersonnels();
}
