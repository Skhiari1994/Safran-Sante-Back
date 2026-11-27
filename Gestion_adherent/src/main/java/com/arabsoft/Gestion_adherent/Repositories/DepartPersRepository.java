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
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                       left join type_depart td
                              on td.cod_typ_depart = p.cod_typ_depart
                      where p.cod_soc = :soc
                        and p.etat_act = 'B'
                      order by p.mat_pers
                     """, nativeQuery = true)
       List<ListPersonnelDepartProjection> getListPersonnelReinteg(@Param("soc") String soc);


       /*
        * Fenêtre de recherche, page par page : même liste que /listPersonnelDepart/{soc} (même requête, mêmes conditions),
        * filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ; date de
        * naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche unique,
        * cherchés dans les colonnes affichées par l'écran (v_xxx = 1).
        */
       @Query(value = """
                     select q.*
                     from (
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
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_nompren is null or lower(cast(q.nomPren as nvarchar(400))) like :f_nompren escape '!')
                       and (:f_cod_lieu_geog is null or lower(cast(q.cod_lieu_geog as nvarchar(400))) like :f_cod_lieu_geog escape '!')
                       and (:f_cod_affect is null or lower(cast(q.cod_affect as nvarchar(400))) like :f_cod_affect escape '!')
                       and (:f_corps is null or lower(cast(q.corps as nvarchar(400))) like :f_corps escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_nompren = 1 and lower(cast(q.nomPren as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_affect = 1 and lower(cast(q.cod_affect as nvarchar(400))) like :l1 escape '!')
                            or (:v_corps = 1 and lower(cast(q.corps as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_nompren = 1 and lower(cast(q.nomPren as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_affect = 1 and lower(cast(q.cod_affect as nvarchar(400))) like :l2 escape '!')
                            or (:v_corps = 1 and lower(cast(q.corps as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_nompren = 1 and lower(cast(q.nomPren as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_affect = 1 and lower(cast(q.cod_affect as nvarchar(400))) like :l3 escape '!')
                            or (:v_corps = 1 and lower(cast(q.corps as nvarchar(400))) like :l3 escape '!'))
                     order by q.mat_pers
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<ListPersonnelDepartProjection> pagePersonnelDepart(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_nompren") String f_nompren,
                     @Param("f_cod_lieu_geog") String f_cod_lieu_geog,
                     @Param("f_cod_affect") String f_cod_affect,
                     @Param("f_corps") String f_corps,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_nompren") int v_nompren,
                     @Param("v_cod_lieu_geog") int v_cod_lieu_geog,
                     @Param("v_cod_affect") int v_cod_affect,
                     @Param("v_corps") int v_corps,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3,
                     @Param("debut") int debut, @Param("taille") int taille);

       @Query(value = """
                     select count(*)
                     from (
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
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_nompren is null or lower(cast(q.nomPren as nvarchar(400))) like :f_nompren escape '!')
                       and (:f_cod_lieu_geog is null or lower(cast(q.cod_lieu_geog as nvarchar(400))) like :f_cod_lieu_geog escape '!')
                       and (:f_cod_affect is null or lower(cast(q.cod_affect as nvarchar(400))) like :f_cod_affect escape '!')
                       and (:f_corps is null or lower(cast(q.corps as nvarchar(400))) like :f_corps escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_nompren = 1 and lower(cast(q.nomPren as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_affect = 1 and lower(cast(q.cod_affect as nvarchar(400))) like :l1 escape '!')
                            or (:v_corps = 1 and lower(cast(q.corps as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_nompren = 1 and lower(cast(q.nomPren as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_affect = 1 and lower(cast(q.cod_affect as nvarchar(400))) like :l2 escape '!')
                            or (:v_corps = 1 and lower(cast(q.corps as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_nompren = 1 and lower(cast(q.nomPren as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_affect = 1 and lower(cast(q.cod_affect as nvarchar(400))) like :l3 escape '!')
                            or (:v_corps = 1 and lower(cast(q.corps as nvarchar(400))) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterPersonnelDepart(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_nompren") String f_nompren,
                     @Param("f_cod_lieu_geog") String f_cod_lieu_geog,
                     @Param("f_cod_affect") String f_cod_affect,
                     @Param("f_corps") String f_corps,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_nompren") int v_nompren,
                     @Param("v_cod_lieu_geog") int v_cod_lieu_geog,
                     @Param("v_cod_affect") int v_cod_affect,
                     @Param("v_corps") int v_corps,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

}