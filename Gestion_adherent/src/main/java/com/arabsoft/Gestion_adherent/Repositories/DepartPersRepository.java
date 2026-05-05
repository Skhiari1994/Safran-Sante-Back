package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.DepartPers;
import com.arabsoft.gestion_adherent.entities.cle.CleDepartPers;
import com.arabsoft.gestion_adherent.projections.DepartPersProjection;
import com.arabsoft.gestion_adherent.projections.ListPersonnelDepartProjection;

import java.util.List;

public interface DepartPersRepository extends JpaRepository<DepartPers, CleDepartPers> {

       @Query(value = """
                     select d.cod_soc,
                            d.mat_pers,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as nomPers,
                            d.cod_typ_depart,
                            td.lib_typ_depart as libDepart,
                            d.dat_depart,
                            d.dat_sais_depart,
                            d.obs_depart,
                            d.etat_depart,
                            d.corps,
                            c.lib_corps as libCorps,
                            d.cod_affect,
                            a.lib_affect as libAffect,
                            d.cod_lieu_geog,
                            lg.lib_lieu as libLieu,
                            p.dat_nais as datNais,
                            p.dat_emb as datEmb
                       from depart_pers d
                       join personnel p
                              on p.mat_pers = d.mat_pers
                             and p.cod_soc = d.cod_soc
                       left join type_depart td
                              on td.cod_typ_depart = d.cod_typ_depart
                       left join corps c
                              on c.cod_corps = d.corps
                       left join affectation a
                              on a.cod_affect = d.cod_affect
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = d.cod_lieu_geog
                      where d.cod_soc = :soc
                        and coalesce(d.cod_typ_depart, 'ZZ') <> '10'
                     """, nativeQuery = true)
       List<DepartPersProjection> getListDepart(@Param("soc") String soc);

       @Query(value = """
                     select d.cod_soc,
                            d.mat_pers,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as nomPers,
                            d.cod_typ_depart,
                            td.lib_typ_depart as libDepart,
                            d.dat_depart,
                            d.dat_sais_depart,
                            d.obs_depart,
                            d.etat_depart,
                            d.corps,
                            c.lib_corps as libCorps,
                            d.cod_affect,
                            a.lib_affect as libAffect,
                            d.cod_lieu_geog,
                            lg.lib_lieu as libLieu,
                            p.dat_nais as datNais,
                            p.dat_emb as datEmb
                       from depart_pers d
                       join personnel p
                              on p.mat_pers = d.mat_pers
                             and p.cod_soc = d.cod_soc
                       left join type_depart td
                              on td.cod_typ_depart = d.cod_typ_depart
                       left join corps c
                              on c.cod_corps = d.corps
                       left join affectation a
                              on a.cod_affect = d.cod_affect
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = d.cod_lieu_geog
                      where d.cod_soc = :soc
                        and d.cod_typ_depart = '10'
                     """, nativeQuery = true)
       List<DepartPersProjection> getListReinteg(@Param("soc") String soc);

       @Query(value = """
                     select d.cod_soc,
                            d.mat_pers,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as nomPers,
                            d.cod_typ_depart,
                            td.lib_typ_depart as libDepart,
                            d.dat_depart,
                            d.dat_sais_depart,
                            d.obs_depart,
                            d.etat_depart,
                            d.corps,
                            c.lib_corps as libCorps,
                            d.cod_affect,
                            a.lib_affect as libAffect,
                            d.cod_lieu_geog,
                            lg.lib_lieu as libLieu,
                            p.dat_nais as datNais,
                            p.dat_emb as datEmb
                       from depart_pers d
                       join personnel p
                              on p.mat_pers = d.mat_pers
                             and p.cod_soc = d.cod_soc
                       left join type_depart td
                              on td.cod_typ_depart = d.cod_typ_depart
                       left join corps c
                              on c.cod_corps = d.corps
                       left join affectation a
                              on a.cod_affect = d.cod_affect
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = d.cod_lieu_geog
                      where d.cod_soc = :soc
                        and coalesce(d.etat_depart, 'I') = 'I'
                     """, nativeQuery = true)
       List<DepartPersProjection> getListDepartReintegInstance(@Param("soc") String soc);

       @Query(value = """
                     select p.mat_pers,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as nomPren,
                            p.cod_affect,
                            a.lib_affect as libAffect,
                            p.cod_lieu_geog,
                            lg.lib_lieu as lib_lieu,
                            p.corps,
                            p.dat_nais,
                            p.dat_emb
                       from personnel p
                       left join affectation a
                              on a.cod_affect = p.cod_affect
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                      where p.cod_soc = :soc
                        and p.etat_act = 'A'
                        and p.cod_typ_depart is null
                      order by p.mat_pers
                     """, nativeQuery = true)
       List<ListPersonnelDepartProjection> getListPersonnelDepart(@Param("soc") String soc);

       @Query(value = """
                     select p.mat_pers,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as nomPren,
                            p.cod_affect,
                            a.lib_affect as libAffect,
                            p.cod_lieu_geog,
                            lg.lib_lieu as lib_lieu,
                            p.corps,
                            p.dat_nais,
                            p.dat_emb,
                            p.cod_typ_depart,
                            td.lib_typ_depart as libDepart,
                            p.dat_depart
                       from personnel p
                       left join affectation a
                              on a.cod_affect = p.cod_affect
                       left join PRM_LIEU_GEOGRAPHIQUE lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                       left join type_depart td
                              on td.cod_typ_depart = p.cod_typ_depart
                      where p.cod_soc = :soc
                        and p.etat_act = 'B'
                      order by p.mat_pers
                     """, nativeQuery = true)
       List<ListPersonnelDepartProjection> getListPersonnelReinteg(@Param("soc") String soc);

}