package com.arabsoft.gestion_adherent.repositories;

import com.arabsoft.gestion_adherent.projections.ComptesSexeProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.Personnel;
import com.arabsoft.gestion_adherent.entities.cle.ClePersonnel;
import com.arabsoft.gestion_adherent.projections.MatIntProjection;
import com.arabsoft.gestion_adherent.projections.PersonnelPrejection;

import java.util.List;

public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

       @Query(value = """
                     select
                            p.cod_soc,
                            p.mat_pers,
                            p.cod_assur,
                            p.nom_pers,
                            p.sexe,
                            p.cin,
                            p.dat_nais,
                            p.pren_pers,
                            p.dat_emb,
                            p.cod_sit,
                            p.nbr_enf,
                            p.cod_retr,
                            p.num_retr,
                            p.num_assur,
                            p.dat_ass,
                            p.cod_pay,
                            p.rib,
                            p.nom_pers_a,
                            p.pren_pers_a,
                            p.cod_natp,
                            p.cod_banq,
                            p.cod_agc,
                            p.dat_dece,
                            p.etat_act,
                            p.dat_motif,
                            p.cod_lieu_geog,
                            p.bas_plafond,
                            p.nom_jf,
                            p.nom_jf_a,
                            p.photo_pers,
                            p.lieu_nais,
                            p.mnt_param,
                            p.etat_prof,
                            p.dat_aff_cnam,
                            p.corps,
                            p.cod_affect,
                            p.dat_affect,
                            p.cod_typ_depart,
                            p.dat_depart,
                            p.typ_aff,
                            p.cod_user,
                            p.dat_maj,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                            lg.lib_lieu as lib_lieu
                       from personnel p
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                      where p.etat_act = 'I'
                        and (:soc = '' or p.cod_soc = :soc)
                        and (:mat = '' or p.mat_pers = :mat)
                      order by cast(p.mat_pers as numeric)
                     """, nativeQuery = true)
       List<PersonnelPrejection> getPersonnelEnInstance(@Param("soc") String soc, @Param("mat") String mat);

       @Query(value = """
                     select
                            p.cod_soc,
                            p.mat_pers,
                            p.cod_assur,
                            p.nom_pers,
                            p.sexe,
                            p.cin,
                            p.dat_nais,
                            p.pren_pers,
                            p.dat_emb,
                            p.cod_sit,
                            p.nbr_enf,
                            p.cod_retr,
                            p.num_retr,
                            p.num_assur,
                            p.dat_ass,
                            p.cod_pay,
                            p.rib,
                            p.nom_pers_a,
                            p.pren_pers_a,
                            p.cod_natp,
                            p.cod_banq,
                            p.cod_agc,
                            p.dat_dece,
                            p.etat_act,
                            p.dat_motif,
                            p.cod_lieu_geog,
                            p.bas_plafond,
                            p.nom_jf,
                            p.nom_jf_a,
                            p.photo_pers,
                            p.lieu_nais,
                            p.mnt_param,
                            p.etat_prof,
                            p.dat_aff_cnam,
                            p.corps,
                            p.cod_affect,
                            p.dat_affect,
                            p.cod_typ_depart,
                            p.dat_depart,
                            p.typ_aff,
                            p.cod_user,
                            p.dat_maj,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                            lg.lib_lieu as libLieu
                       from personnel p
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                      where p.cod_soc = :soc
                        and p.mat_pers = :mat
                      order by cast(p.mat_pers as numeric)
                     """, nativeQuery = true)
       List<PersonnelPrejection> getPersonnelByMat(@Param("soc") String mat, @Param("mat") String soc);

       @Query(value = """
                     select
                            p.cod_soc,
                            p.mat_pers,
                            p.cod_assur,
                            p.nom_pers,
                            p.sexe,
                            p.cin,
                            p.dat_nais,
                            p.pren_pers,
                            p.dat_emb,
                            p.cod_sit,
                            p.nbr_enf,
                            p.cod_retr,
                            p.num_retr,
                            p.num_assur,
                            p.dat_ass,
                            p.cod_pay,
                            p.rib,
                            p.nom_pers_a,
                            p.pren_pers_a,
                            p.cod_natp,
                            p.cod_banq,
                            p.cod_agc,
                            p.dat_dece,
                            p.etat_act,
                            p.dat_motif,
                            p.cod_lieu_geog,
                            p.bas_plafond,
                            p.nom_jf,
                            p.nom_jf_a,
                            p.photo_pers,
                            p.lieu_nais,
                            p.mnt_param,
                            p.etat_prof,
                            p.dat_aff_cnam,
                            p.corps,
                            p.cod_affect,
                            p.dat_affect,
                            p.cod_typ_depart,
                            p.dat_depart,
                            p.typ_aff,
                            p.cod_user,
                            p.dat_maj,
                            concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                            lg.lib_lieu as libLieu
                       from personnel p
                       left join prm_lieu_geographique lg
                              on lg.cod_lieu_geog = p.cod_lieu_geog
                      where (:soc = '' or p.cod_soc = :soc)
                        and (:mat = '' or p.mat_pers = :mat)
                      order by cast(p.mat_pers as numeric)
                     """, nativeQuery = true)
       List<PersonnelPrejection> getPersonnels(@Param("soc") String soc, @Param("mat") String mat);

       /** Personnel d'une société (et d'un seul matricule si mat est renseigné). */
       @Query("""
                     select p
                     from Personnel p
                     where p.cod_soc = :soc
                       and (:mat = '' or p.mat_pers = :mat)
                     """)
       List<Personnel> findBySociete(
                     @Param("soc") String soc,
                     @Param("mat") String mat);

       @Query(value = "select count(*) from personnel p where p.cod_soc = :soc and p.mat_pers = :mat ", nativeQuery = true)
       Long countCodSocAndMatPers(@Param("soc") String codSoc, @Param("mat") String matPers);

       @Query(value = """
                     select p.mat_pers matPers,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) nomPren
                       from personnel p
                      order by p.cod_soc, cast(p.mat_pers as numeric)
                     """, nativeQuery = true)
       List<MatIntProjection> getMatInt();


       /*
        * Fenêtre de recherche, page par page : même liste que /getPersonnelles (findAll / findBySociete, fiche complète) (même requête, mêmes conditions),
        * filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ; date de
        * naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche unique,
        * cherchés dans les colonnes affichées par l'écran (v_xxx = 1). sexe : M ou F.
        */
       @Query(value = """
                     select q.*
                     from (
                       select p.*
                       from personnel p
                       where (:soc = '' or p.cod_soc = :soc)
                         and (:mat = '' or p.mat_pers = :mat)
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :f_dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :f_dat_nais escape '!'))
                       and (:f_nom_pers is null or lower(cast(q.nom_pers as nvarchar(400))) like :f_nom_pers escape '!')
                       and (:f_pren_pers is null or lower(cast(q.pren_pers as nvarchar(400))) like :f_pren_pers escape '!')
                       and (:f_num_retr is null or lower(cast(q.num_retr as nvarchar(400))) like :f_num_retr escape '!')
                       and (:f_cin is null or lower(cast(q.cin as nvarchar(400))) like :f_cin escape '!')
                       and (:f_cod_lieu_geog is null or lower(cast(q.cod_lieu_geog as nvarchar(400))) like :f_cod_lieu_geog escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l1 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l2 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l3 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l3 escape '!'))
                     order by q.cod_soc, case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end, q.mat_pers
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<Personnel> pagePersonnelles(@Param("soc") String soc,
                     @Param("mat") String mat,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_dat_nais") String f_dat_nais,
                     @Param("f_nom_pers") String f_nom_pers,
                     @Param("f_pren_pers") String f_pren_pers,
                     @Param("f_num_retr") String f_num_retr,
                     @Param("f_cin") String f_cin,
                     @Param("f_cod_lieu_geog") String f_cod_lieu_geog,
                     @Param("sexe") String sexe,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_cin") int v_cin,
                     @Param("v_cod_lieu_geog") int v_cod_lieu_geog,
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
                       select p.*
                       from personnel p
                       where (:soc = '' or p.cod_soc = :soc)
                         and (:mat = '' or p.mat_pers = :mat)
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :f_dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :f_dat_nais escape '!'))
                       and (:f_nom_pers is null or lower(cast(q.nom_pers as nvarchar(400))) like :f_nom_pers escape '!')
                       and (:f_pren_pers is null or lower(cast(q.pren_pers as nvarchar(400))) like :f_pren_pers escape '!')
                       and (:f_num_retr is null or lower(cast(q.num_retr as nvarchar(400))) like :f_num_retr escape '!')
                       and (:f_cin is null or lower(cast(q.cin as nvarchar(400))) like :f_cin escape '!')
                       and (:f_cod_lieu_geog is null or lower(cast(q.cod_lieu_geog as nvarchar(400))) like :f_cod_lieu_geog escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l1 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l2 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l3 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterPersonnelles(@Param("soc") String soc,
                     @Param("mat") String mat,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_dat_nais") String f_dat_nais,
                     @Param("f_nom_pers") String f_nom_pers,
                     @Param("f_pren_pers") String f_pren_pers,
                     @Param("f_num_retr") String f_num_retr,
                     @Param("f_cin") String f_cin,
                     @Param("f_cod_lieu_geog") String f_cod_lieu_geog,
                     @Param("sexe") String sexe,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_cin") int v_cin,
                     @Param("v_cod_lieu_geog") int v_cod_lieu_geog,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

       /** Effectifs du filtre Sexe (sur toute la liste, comme avant) : Tous, Masculin, Féminin. */
       @Query(value = """
                     select cast(count(*) as bigint) as tous,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'M' then 1 else 0 end), 0) as bigint) as m,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'F' then 1 else 0 end), 0) as bigint) as f
                     from (
                       select p.*
                       from personnel p
                       where (:soc = '' or p.cod_soc = :soc)
                         and (:mat = '' or p.mat_pers = :mat)
                     ) q
                     """, nativeQuery = true)
       ComptesSexeProjection sexesPersonnelles(@Param("soc") String soc, @Param("mat") String mat);


       /*
        * Fenêtre de recherche, page par page : même liste que /getPersonnels (même requête, mêmes conditions),
        * filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ; date de
        * naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche unique,
        * cherchés dans les colonnes affichées par l'écran (v_xxx = 1). sexe : M ou F.
        */
       @Query(value = """
                     select q.*
                     from (
                       select
                       p.cod_soc,
                       p.mat_pers,
                       p.cod_assur,
                       p.nom_pers,
                       p.sexe,
                       p.cin,
                       p.dat_nais,
                       p.pren_pers,
                       p.dat_emb,
                       p.cod_sit,
                       p.nbr_enf,
                       p.cod_retr,
                       p.num_retr,
                       p.num_assur,
                       p.dat_ass,
                       p.cod_pay,
                       p.rib,
                       p.nom_pers_a,
                       p.pren_pers_a,
                       p.cod_natp,
                       p.cod_banq,
                       p.cod_agc,
                       p.dat_dece,
                       p.etat_act,
                       p.dat_motif,
                       p.cod_lieu_geog,
                       p.bas_plafond,
                       p.nom_jf,
                       p.nom_jf_a,
                       p.photo_pers,
                       p.lieu_nais,
                       p.mnt_param,
                       p.etat_prof,
                       p.dat_aff_cnam,
                       p.corps,
                       p.cod_affect,
                       p.dat_affect,
                       p.cod_typ_depart,
                       p.dat_depart,
                       p.typ_aff,
                       p.cod_user,
                       p.dat_maj,
                       concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                       lg.lib_lieu as libLieu
                       from personnel p
                       left join prm_lieu_geographique lg
                       on lg.cod_lieu_geog = p.cod_lieu_geog
                       where (:soc = '' or p.cod_soc = :soc)
                       and (:mat = '' or p.mat_pers = :mat)
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :f_dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :f_dat_nais escape '!'))
                       and (:f_full_name is null or lower(cast(q.full_name as nvarchar(400))) like :f_full_name escape '!')
                       and (:f_nom_pers is null or lower(cast(q.nom_pers as nvarchar(400))) like :f_nom_pers escape '!')
                       and (:f_pren_pers is null or lower(cast(q.pren_pers as nvarchar(400))) like :f_pren_pers escape '!')
                       and (:f_num_retr is null or lower(cast(q.num_retr as nvarchar(400))) like :f_num_retr escape '!')
                       and (:f_cin is null or lower(cast(q.cin as nvarchar(400))) like :f_cin escape '!')
                       and (:f_cod_lieu_geog is null or lower(cast(q.cod_lieu_geog as nvarchar(400))) like :f_cod_lieu_geog escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_full_name = 1 and lower(cast(q.full_name as nvarchar(400))) like :l1 escape '!')
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l1 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_full_name = 1 and lower(cast(q.full_name as nvarchar(400))) like :l2 escape '!')
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l2 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_full_name = 1 and lower(cast(q.full_name as nvarchar(400))) like :l3 escape '!')
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l3 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l3 escape '!'))
                     order by case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end, q.mat_pers, q.cod_soc
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<PersonnelPrejection> pagePersonnels(@Param("soc") String soc,
                     @Param("mat") String mat,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_dat_nais") String f_dat_nais,
                     @Param("f_full_name") String f_full_name,
                     @Param("f_nom_pers") String f_nom_pers,
                     @Param("f_pren_pers") String f_pren_pers,
                     @Param("f_num_retr") String f_num_retr,
                     @Param("f_cin") String f_cin,
                     @Param("f_cod_lieu_geog") String f_cod_lieu_geog,
                     @Param("sexe") String sexe,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_full_name") int v_full_name,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_cin") int v_cin,
                     @Param("v_cod_lieu_geog") int v_cod_lieu_geog,
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
                       select
                       p.cod_soc,
                       p.mat_pers,
                       p.cod_assur,
                       p.nom_pers,
                       p.sexe,
                       p.cin,
                       p.dat_nais,
                       p.pren_pers,
                       p.dat_emb,
                       p.cod_sit,
                       p.nbr_enf,
                       p.cod_retr,
                       p.num_retr,
                       p.num_assur,
                       p.dat_ass,
                       p.cod_pay,
                       p.rib,
                       p.nom_pers_a,
                       p.pren_pers_a,
                       p.cod_natp,
                       p.cod_banq,
                       p.cod_agc,
                       p.dat_dece,
                       p.etat_act,
                       p.dat_motif,
                       p.cod_lieu_geog,
                       p.bas_plafond,
                       p.nom_jf,
                       p.nom_jf_a,
                       p.photo_pers,
                       p.lieu_nais,
                       p.mnt_param,
                       p.etat_prof,
                       p.dat_aff_cnam,
                       p.corps,
                       p.cod_affect,
                       p.dat_affect,
                       p.cod_typ_depart,
                       p.dat_depart,
                       p.typ_aff,
                       p.cod_user,
                       p.dat_maj,
                       concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                       lg.lib_lieu as libLieu
                       from personnel p
                       left join prm_lieu_geographique lg
                       on lg.cod_lieu_geog = p.cod_lieu_geog
                       where (:soc = '' or p.cod_soc = :soc)
                       and (:mat = '' or p.mat_pers = :mat)
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :f_dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :f_dat_nais escape '!'))
                       and (:f_full_name is null or lower(cast(q.full_name as nvarchar(400))) like :f_full_name escape '!')
                       and (:f_nom_pers is null or lower(cast(q.nom_pers as nvarchar(400))) like :f_nom_pers escape '!')
                       and (:f_pren_pers is null or lower(cast(q.pren_pers as nvarchar(400))) like :f_pren_pers escape '!')
                       and (:f_num_retr is null or lower(cast(q.num_retr as nvarchar(400))) like :f_num_retr escape '!')
                       and (:f_cin is null or lower(cast(q.cin as nvarchar(400))) like :f_cin escape '!')
                       and (:f_cod_lieu_geog is null or lower(cast(q.cod_lieu_geog as nvarchar(400))) like :f_cod_lieu_geog escape '!')
                       and (:sexe is null or upper(ltrim(rtrim(q.sexe))) = :sexe)
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_full_name = 1 and lower(cast(q.full_name as nvarchar(400))) like :l1 escape '!')
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l1 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l1 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_full_name = 1 and lower(cast(q.full_name as nvarchar(400))) like :l2 escape '!')
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l2 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l2 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_full_name = 1 and lower(cast(q.full_name as nvarchar(400))) like :l3 escape '!')
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(cast(q.num_retr as nvarchar(400))) like :l3 escape '!')
                            or (:v_cin = 1 and lower(cast(q.cin as nvarchar(400))) like :l3 escape '!')
                            or (:v_cod_lieu_geog = 1 and lower(cast(q.cod_lieu_geog as nvarchar(400))) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterPersonnels(@Param("soc") String soc,
                     @Param("mat") String mat,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_dat_nais") String f_dat_nais,
                     @Param("f_full_name") String f_full_name,
                     @Param("f_nom_pers") String f_nom_pers,
                     @Param("f_pren_pers") String f_pren_pers,
                     @Param("f_num_retr") String f_num_retr,
                     @Param("f_cin") String f_cin,
                     @Param("f_cod_lieu_geog") String f_cod_lieu_geog,
                     @Param("sexe") String sexe,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_full_name") int v_full_name,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_cin") int v_cin,
                     @Param("v_cod_lieu_geog") int v_cod_lieu_geog,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

       /** Effectifs du filtre Sexe (sur toute la liste, comme avant) : Tous, Masculin, Féminin. */
       @Query(value = """
                     select cast(count(*) as bigint) as tous,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'M' then 1 else 0 end), 0) as bigint) as m,
                            cast(coalesce(sum(case when upper(ltrim(rtrim(q.sexe))) = 'F' then 1 else 0 end), 0) as bigint) as f
                     from (
                       select
                       p.cod_soc,
                       p.mat_pers,
                       p.cod_assur,
                       p.nom_pers,
                       p.sexe,
                       p.cin,
                       p.dat_nais,
                       p.pren_pers,
                       p.dat_emb,
                       p.cod_sit,
                       p.nbr_enf,
                       p.cod_retr,
                       p.num_retr,
                       p.num_assur,
                       p.dat_ass,
                       p.cod_pay,
                       p.rib,
                       p.nom_pers_a,
                       p.pren_pers_a,
                       p.cod_natp,
                       p.cod_banq,
                       p.cod_agc,
                       p.dat_dece,
                       p.etat_act,
                       p.dat_motif,
                       p.cod_lieu_geog,
                       p.bas_plafond,
                       p.nom_jf,
                       p.nom_jf_a,
                       p.photo_pers,
                       p.lieu_nais,
                       p.mnt_param,
                       p.etat_prof,
                       p.dat_aff_cnam,
                       p.corps,
                       p.cod_affect,
                       p.dat_affect,
                       p.cod_typ_depart,
                       p.dat_depart,
                       p.typ_aff,
                       p.cod_user,
                       p.dat_maj,
                       concat(p.pren_pers, concat(' ', p.nom_pers)) as full_name,
                       lg.lib_lieu as libLieu
                       from personnel p
                       left join prm_lieu_geographique lg
                       on lg.cod_lieu_geog = p.cod_lieu_geog
                       where (:soc = '' or p.cod_soc = :soc)
                       and (:mat = '' or p.mat_pers = :mat)
                     ) q
                     """, nativeQuery = true)
       ComptesSexeProjection sexesPersonnels(@Param("soc") String soc, @Param("mat") String mat);

}
