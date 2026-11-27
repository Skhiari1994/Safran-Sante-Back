package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.Personnel;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePersonnel;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings({ "java:S117" })
public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

  @Query(value = """
      select
          p.num_retr,
          p.cod_retr,
          p.mat_pers,
          p.dat_nais,
          p.num_assur,
          concat(p.nom_pers, concat(' ', p.pren_pers)) AS nom,
          p.cod_assur,
          a.lib_assur,
          f.cod_fil,
          rf.lib_fill
      from personnel p
      inner join assurance a on p.cod_assur = a.cod_assur
      inner join pers_affil f on p.mat_pers = f.mat_pers and p.cod_soc = f.cod_soc
      inner join ref_filliere rf on f.cod_fil = rf.cod_fil
      where p.cod_soc = :soc
        and p.etat_act = 'A'
        and f.courant = 'O'
        and coalesce(rf.bult_mut, 'N') = 'O'
      order by cast(p.mat_pers as numeric)
      """, nativeQuery = true)
  List<PersonnelBultSoinProjection> getPersonnelBultSoin(@Param("soc") String soc);

  @Query(value = """
      select num_fam, nom_pren, dat_naiss as dat_nais
      from famille
      where cod_soc = :soc
        and mat_pers = :mat
        and (coalesce(pec, 'N') = 'O' or coalesce(pec_mut, 'N') = 'O')
      order by num_fam
      """, nativeQuery = true)
  List<AdherentProjection> getListAdherent(@Param("soc") String soc, @Param("mat") String mat);

  @Query(value = """
      select 0 as num_fam, concat(nom_pers, concat(' ',pren_pers)) as nom_pren, dat_nais
      from personnel
      where cod_soc = :soc
        and mat_pers = :mat
      """, nativeQuery = true)
  AdherentProjection getAdherentLui(@Param("soc") String soc, @Param("mat") String mat);

  @Query(value = """
      select num_fam, nom_pren
      from famille
      where cod_soc = :soc
        and mat_pers = :mat
        and (coalesce(pec, 'N') = 'O' or coalesce(pec_mut, 'N') = 'O')
      order by num_fam
      """, nativeQuery = true)
  List<AdherentProjection> getListAdherentDossMld(@Param("soc") String soc, @Param("mat") String mat);

  @Query(value = """
      select num_fam, nom_pren
      from famille
      where cod_soc = :soc
        and mat_pers = :mat
        and num_fam = :fam
        and (coalesce(pec, 'N') = 'O' or coalesce(pec_mut, 'N') = 'O')
      order by num_fam
      """, nativeQuery = true)
  List<AdherentProjection> getListAdherentFamille(@Param("soc") String soc,
      @Param("mat") String mat,
      @Param("fam") Integer fam);

  @Query(value = """
      select
          p.num_retr,
          p.mat_pers,
          concat(p.nom_pers, concat(' ', p.pren_pers)) as nom,
          a.cod_fil
      from personnel p
      inner join pers_affil a
              on p.cod_soc = a.cod_soc
             and p.mat_pers = a.mat_pers
      where p.cod_soc = :soc
        and a.courant = 'O'
      """, nativeQuery = true)
  List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisie(@Param("soc") String soc);

  @Query(value = """
      select
          f.num_fam as num_fam,
          f.nom_pren as nom,
          f.dat_naiss as dat_naiss
      from famille f
      where f.cod_soc = :soc
        and f.mat_pers = :pers
        and coalesce(pec_mut, 'N') = 'O'
        and parente in ('P','M','C')

      union

      select
          l.num_fam as num_fam,
          l.nom_pren as nom,
          l.dat_naiss as dat_naiss
      from famille l
      where l.cod_soc = :soc
        and l.mat_pers = :pers
        and coalesce(pec, 'N') = 'O'
        and parente = 'E'

      union

      select
          0 as num_fam,
          'Adhérent' as nom,
          dat_nais as dat_naiss
      from personnel
      where cod_soc = :soc
        and mat_pers = :pers
      """, nativeQuery = true)
  List<FamillePersonnelBultSoin> getListFamillePersonnelBultSoinLibre(@Param("soc") String soc,
      @Param("pers") String pers);

  @Query(value = """
      select
          p.num_retr,
          p.mat_pers,
          concat(p.nom_pers, concat(' ', p.pren_pers)) as nom,
          a.cod_fil
      from personnel p
      inner join pers_affil a
              on p.cod_soc = a.cod_soc
             and p.mat_pers = a.mat_pers
      where p.cod_soc = :soc
        and p.cod_assur = :ass
        and p.etat_act = 'A'
        and a.courant = 'O'
      """, nativeQuery = true)
  List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieLibre(@Param("soc") String soc,
      @Param("ass") String ass);

  @Query(value = """
      select
          p.num_retr,
          b.mat_pers,
          concat(p.nom_pers, concat(' ', p.pren_pers)) as nom,
          b.cod_fil
      from personnel p
      inner join bult_soin b
              on p.cod_soc = b.cod_soc
             and p.mat_pers = b.mat_pers
      where b.cod_soc = :soc
        and b.cod_bord = :cod_bord
        and b.cod_assur = :ass
      """, nativeQuery = true)
  List<PersonnelBultSoinProjection> getPersonnelBultSoinSaisieCnam(@Param("soc") String soc,
      @Param("ass") String mat,
      @Param("cod_bord") String cod_bord);

  @Query(value = """
      select distinct
          b.num_fam,
          case
              when b.num_fam = 0 then 'Adhérent'
              else f.nom_pren
          end as nom,
          b.dat_soin,
          f.dat_naiss
      from famille f
      join bult_soin b
        on f.cod_soc = b.cod_soc
       and f.mat_pers = b.mat_pers
       and f.num_fam = b.num_fam
      where b.cod_soc = :soc
        and b.mat_pers = :mat
        and b.cod_bord = coalesce(:cod_bord, b.cod_bord)

      union

      select distinct
          b.num_fam,
          'Adhérent',
          b.dat_soin,
          p.dat_nais
      from bult_soin b
      join personnel p
        on b.cod_soc = p.cod_soc
       and b.mat_pers = p.mat_pers
      where b.cod_soc = :soc
        and b.mat_pers = :mat
        and b.num_fam = 0
        and b.cod_bord = coalesce(:cod_bord, b.cod_bord)
      """, nativeQuery = true)
  List<FamillePersonnelBultSoin> getPrestat(@Param("soc") String soc,
      @Param("mat") String mat,
      @Param("cod_bord") String cod_bord);

  @Query(value = """
      select
          p.*,
          a.lib_assur
      from personnel p
      left join assurance a on a.cod_assur = p.cod_assur
      where p.cod_soc = :soc and p.etat_act = 'A' order by cast(p.mat_pers as int)
      """, nativeQuery = true)
  List<PersonnelProjection> getPersActif(@Param("soc") String soc);

  @Query(value = """
      select
          mat_pers,
          dat_nais,
          num_retr,
          concat(nom_pers, concat(' ', pren_pers)) as nom
      from personnel
      where cod_soc = :soc
        and etat_act = 'A' order by cast(mat_pers as int)
      """, nativeQuery = true)
  List<PersonnelProjection> getPersBultCnamDeb(@Param("soc") String soc);

  @Query(value = """
      select
          mat_pers,
          dat_nais,
          num_retr,
          concat(nom_pers, concat(' ', pren_pers)) as nom
      from personnel
      where cod_soc = :soc
        and etat_act = 'A' order by cast(mat_pers as int)
      """, nativeQuery = true)
  List<PersonnelProjection> getPersBultCnamFin(@Param("soc") String soc);

  @Query(value = """
          select p.dat_nais
          from personnel p
          where p.cod_soc = :codSoc
            and p.mat_pers = :matPers
      """, nativeQuery = true)
  LocalDate findDatNais(
      @Param("codSoc") String codSoc,
      @Param("matPers") String matPers);

  @Query(value = """
      select *
      from personnel p
      where p.cod_soc = :soc
        and p.etat_act = 'A'
      order by cast(p.mat_pers as int)
      """, nativeQuery = true)
  List<Personnel> getPersActifBySoc(@Param("soc") String soc);


       /*
        * Fenêtre de recherche « personnel », page par page : même liste que getPersActif (mêmes colonnes, mêmes
        * conditions), filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ;
        * date de naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche
        * unique, cherchés dans les colonnes affichées par l'écran (v_xxx = 1). Ordre : matricule (numérique).
        */
       @Query(value = """
                     select q.*
                     from (
                     select p.*, a.lib_assur
                     from personnel p
                     left join assurance a on a.cod_assur = p.cod_assur
                     where p.cod_soc = :soc and p.etat_act = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:nom_pers is null or lower(q.nom_pers) like :nom_pers escape '!')
                       and (:pren_pers is null or lower(q.pren_pers) like :pren_pers escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l3 escape '!'))
                     order by case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end,
                              q.mat_pers
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<PersonnelProjection> pagePersActif(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("num_retr") String num_retr,
                     @Param("nom_pers") String nom_pers,
                     @Param("pren_pers") String pren_pers,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
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
                     select p.*, a.lib_assur
                     from personnel p
                     left join assurance a on a.cod_assur = p.cod_assur
                     where p.cod_soc = :soc and p.etat_act = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:nom_pers is null or lower(q.nom_pers) like :nom_pers escape '!')
                       and (:pren_pers is null or lower(q.pren_pers) like :pren_pers escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterPersActif(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("num_retr") String num_retr,
                     @Param("nom_pers") String nom_pers,
                     @Param("pren_pers") String pren_pers,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

       /*
        * Fenêtre de recherche « personnel », page par page : même liste que getPersActifBySoc (GetAllPers) (mêmes colonnes, mêmes
        * conditions), filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ;
        * date de naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche
        * unique, cherchés dans les colonnes affichées par l'écran (v_xxx = 1). Ordre : matricule (numérique).
        */
       @Query(value = """
                     select q.*
                     from (
                     select p.*
                     from personnel p
                     where p.cod_soc = :soc
                       and p.etat_act = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:nom_pers is null or lower(q.nom_pers) like :nom_pers escape '!')
                       and (:pren_pers is null or lower(q.pren_pers) like :pren_pers escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l3 escape '!'))
                     order by case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end,
                              q.mat_pers
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<Personnel> pagePersActifEntite(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("num_retr") String num_retr,
                     @Param("nom_pers") String nom_pers,
                     @Param("pren_pers") String pren_pers,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
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
                     where p.cod_soc = :soc
                       and p.etat_act = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:nom_pers is null or lower(q.nom_pers) like :nom_pers escape '!')
                       and (:pren_pers is null or lower(q.pren_pers) like :pren_pers escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!')
                            or (:v_nom_pers = 1 and lower(q.nom_pers) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(q.pren_pers) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterPersActifEntite(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("num_retr") String num_retr,
                     @Param("nom_pers") String nom_pers,
                     @Param("pren_pers") String pren_pers,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

       /*
        * Fenêtre de recherche « personnel », page par page : même liste que getPersBultCnamDeb / getPersBultCnamFin (mêmes colonnes, mêmes
        * conditions), filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ;
        * date de naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche
        * unique, cherchés dans les colonnes affichées par l'écran (v_xxx = 1). Ordre : matricule (numérique).
        */
       @Query(value = """
                     select q.*
                     from (
                     select mat_pers,
                            dat_nais,
                            num_retr,
                            concat(nom_pers, concat(' ', pren_pers)) as nom
                     from personnel
                     where cod_soc = :soc
                       and etat_act = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:nom is null or lower(q.nom) like :nom escape '!')
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!'))
                     order by case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end,
                              q.mat_pers
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<PersonnelProjection> pagePersBultCnam(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("nom") String nom,
                     @Param("num_retr") String num_retr,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_nom") int v_nom,
                     @Param("v_num_retr") int v_num_retr,
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
                     select mat_pers,
                            dat_nais,
                            num_retr,
                            concat(nom_pers, concat(' ', pren_pers)) as nom
                     from personnel
                     where cod_soc = :soc
                       and etat_act = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:nom is null or lower(q.nom) like :nom escape '!')
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterPersBultCnam(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("nom") String nom,
                     @Param("num_retr") String num_retr,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_nom") int v_nom,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

       /*
        * Fenêtre de recherche « personnel », page par page : même liste que getPersonnelBultSoin (mêmes colonnes, mêmes
        * conditions), filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ;
        * date de naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche
        * unique, cherchés dans les colonnes affichées par l'écran (v_xxx = 1). Ordre : matricule (numérique).
        */
       @Query(value = """
                     select q.*
                     from (
                     select p.num_retr,
                            p.cod_retr,
                            p.mat_pers,
                            p.dat_nais,
                            p.num_assur,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) AS nom,
                            p.cod_assur,
                            a.lib_assur,
                            f.cod_fil,
                            rf.lib_fill
                     from personnel p
                     inner join assurance a on p.cod_assur = a.cod_assur
                     inner join pers_affil f on p.mat_pers = f.mat_pers and p.cod_soc = f.cod_soc
                     inner join ref_filliere rf on f.cod_fil = rf.cod_fil
                     where p.cod_soc = :soc
                       and p.etat_act = 'A'
                       and f.courant = 'O'
                       and coalesce(rf.bult_mut, 'N') = 'O'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:nom is null or lower(q.nom) like :nom escape '!')
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:lib_fill is null or lower(q.lib_fill) like :lib_fill escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!')
                            or (:v_lib_fill = 1 and lower(q.lib_fill) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!')
                            or (:v_lib_fill = 1 and lower(q.lib_fill) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!')
                            or (:v_lib_fill = 1 and lower(q.lib_fill) like :l3 escape '!'))
                     order by case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end,
                              q.mat_pers, q.cod_fil
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<PersonnelBultSoinProjection> pagePersonnelBultSoin(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("nom") String nom,
                     @Param("num_retr") String num_retr,
                     @Param("lib_fill") String lib_fill,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_nom") int v_nom,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_lib_fill") int v_lib_fill,
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
                     select p.num_retr,
                            p.cod_retr,
                            p.mat_pers,
                            p.dat_nais,
                            p.num_assur,
                            concat(p.nom_pers, concat(' ', p.pren_pers)) AS nom,
                            p.cod_assur,
                            a.lib_assur,
                            f.cod_fil,
                            rf.lib_fill
                     from personnel p
                     inner join assurance a on p.cod_assur = a.cod_assur
                     inner join pers_affil f on p.mat_pers = f.mat_pers and p.cod_soc = f.cod_soc
                     inner join ref_filliere rf on f.cod_fil = rf.cod_fil
                     where p.cod_soc = :soc
                       and p.etat_act = 'A'
                       and f.courant = 'O'
                       and coalesce(rf.bult_mut, 'N') = 'O'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :mat_pers)
                       and (:dat_nais is null or (convert(varchar(10), q.dat_nais, 103) like :dat_nais escape '!' or convert(varchar(10), q.dat_nais, 120) like :dat_nais escape '!'))
                       and (:nom is null or lower(q.nom) like :nom escape '!')
                       and (:num_retr is null or lower(q.num_retr) like :num_retr escape '!')
                       and (:lib_fill is null or lower(q.lib_fill) like :lib_fill escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l1 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l1 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l1 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l1 escape '!')
                            or (:v_lib_fill = 1 and lower(q.lib_fill) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l2 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l2 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l2 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l2 escape '!')
                            or (:v_lib_fill = 1 and lower(q.lib_fill) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_dat_nais = 1 and (convert(varchar(10), q.dat_nais, 103) like :l3 escape '!' or convert(varchar(10), q.dat_nais, 120) like :l3 escape '!'))
                            or (:v_nom = 1 and lower(q.nom) like :l3 escape '!')
                            or (:v_num_retr = 1 and lower(q.num_retr) like :l3 escape '!')
                            or (:v_lib_fill = 1 and lower(q.lib_fill) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterPersonnelBultSoin(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("mat_pers") String mat_pers,
                     @Param("dat_nais") String dat_nais,
                     @Param("nom") String nom,
                     @Param("num_retr") String num_retr,
                     @Param("lib_fill") String lib_fill,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_dat_nais") int v_dat_nais,
                     @Param("v_nom") int v_nom,
                     @Param("v_num_retr") int v_num_retr,
                     @Param("v_lib_fill") int v_lib_fill,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

}