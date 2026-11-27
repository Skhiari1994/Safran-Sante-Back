package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.arabsoft.gestion_adherent.entities.AffilMutuelle;
import com.arabsoft.gestion_adherent.entities.cle.CleAffilMutuelle;
import com.arabsoft.gestion_adherent.projections.AffilPersProjection;
import com.arabsoft.gestion_adherent.projections.AffilPersValid;

import java.time.LocalDate;
import java.util.List;

@Repository
@SuppressWarnings({ "java:S117" })
public interface AffilMutuelleRepository extends JpaRepository<AffilMutuelle, CleAffilMutuelle> {

  @Query(value = "select * from affil_mutuelle order by mat_pers", nativeQuery = true)
  List<AffilMutuelle> getAffilMutuelle();

  @Query(value = """
      select mat_pers,
             cin,
             nom_pers,
             pren_pers,
             etat_act,
             dat_emb,
             cod_sit,
             nbr_enf,
             num_assur,
             corps
        from personnel
       where cod_soc = :soc
         and coalesce(typ_aff, 'S') = 'A'
       order by cast(mat_pers as numeric)
      """, nativeQuery = true)
  List<AffilPersProjection> getAffilPers(@Param("soc") String soc);

  @Query(value = """
      select mat_pers,
             cin,
             nom_pers,
             pren_pers,
             etat_act,
             dat_emb,
             cod_sit,
             nbr_enf,
             num_assur,
             corps
        from personnel
       where cod_soc = :soc
         and coalesce(typ_aff, 'S') = 'S'
       order by cast(mat_pers as numeric)
      """, nativeQuery = true)
  List<AffilPersProjection> getAffilPersMut(@Param("soc") String soc);

  @Query(value = """
      select mat_pers,
             cin,
             nom_pers,
             pren_pers,
             etat_act,
             dat_emb,
             cod_sit,
             nbr_enf,
             num_assur,
             corps,
             cod_affect,
             cod_typ_depart
        from personnel
       where cod_soc = :soc
         and mat_pers = :mat
         and coalesce(typ_aff, 'S') = 'A'
       order by cast(mat_pers as numeric)
      """, nativeQuery = true)
  AffilPersProjection getAffilPersByMat(@Param("soc") String soc,
      @Param("mat") String mat);

  @Query(value = """
      select *
        from affil_mutuelle
       where cod_soc = :soc
         and typ_aff = 'A'
         and etat_aff = 'I'
       order by cast(mat_pers as numeric), dat_ass
      """, nativeQuery = true)
  List<AffilMutuelle> getAffilPersRenouvAdhesion(@Param("soc") String soc);

  @Query(value = """
      select t.cod_soc,
             concat(p.nom_pers, concat(' ', p.pren_pers)) as nompren,
             t.mat_pers,
             t.dat_ass,
             t.num_assur,
             t.typ_aff,
             t.dat_dem,
             t.obs_aff,
             t.coef_cot,
             t.etat_aff,
             t.corps,
             t.cod_typ_depart,
             t.cod_affect
        from affil_mutuelle t
        join personnel p on
        t.cod_soc = p.cod_soc
        and t.mat_pers = p.mat_pers
       where t.cod_soc = :soc
         and coalesce(t.etat_aff, 'I') = 'I'
       order by cast(t.mat_pers as numeric), t.dat_ass
      """, nativeQuery = true)
  List<AffilPersValid> getAffilPersEnInstance(@Param("soc") String soc);

  @Query(value = """
      select t.cod_soc,
             t.mat_pers,
             t.dat_ass,
             t.num_assur,
             t.typ_aff,
             t.dat_dem,
             t.obs_aff,
             t.coef_cot,
             t.etat_aff,
             t.corps,
             t.cod_typ_depart,
             t.cod_affect
      from affil_mutuelle t
      where cod_soc = :soc
        and coalesce(etat_aff, 'I') = 'I'
        and mat_pers = :mat
        and dat_ass = :dat_ass
      order by cast(t.mat_pers as numeric), dat_ass
      """, nativeQuery = true)
  AffilMutuelle getAffilPersAffiliation(
      @Param("soc") String soc,
      @Param("mat") String mat,
      @Param("dat_ass") LocalDate dat_ass);


       /*
        * Fenêtre de recherche, page par page : même liste que /getAffilPers/{soc} (même requête, mêmes conditions),
        * filtrée et découpée par la base. code : matricule exact (champ) ; matricule : égalité exacte ; date de
        * naissance : jj/mm/aaaa ; autres colonnes : « contient » ; m1..m3 / l1..l3 : mots de la recherche unique,
        * cherchés dans les colonnes affichées par l'écran (v_xxx = 1).
        */
       @Query(value = """
                     select q.*
                     from (
                       select mat_pers,
                       cin,
                       nom_pers,
                       pren_pers,
                       etat_act,
                       dat_emb,
                       cod_sit,
                       nbr_enf,
                       num_assur,
                       corps
                       from personnel
                       where cod_soc = :soc
                       and coalesce(typ_aff, 'S') = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_nom_pers is null or lower(cast(q.nom_pers as nvarchar(400))) like :f_nom_pers escape '!')
                       and (:f_pren_pers is null or lower(cast(q.pren_pers as nvarchar(400))) like :f_pren_pers escape '!')
                       and (:f_etat_act is null or lower(cast(q.etat_act as nvarchar(400))) like :f_etat_act escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_etat_act = 1 and lower(cast(q.etat_act as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_etat_act = 1 and lower(cast(q.etat_act as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_etat_act = 1 and lower(cast(q.etat_act as nvarchar(400))) like :l3 escape '!'))
                     order by case when q.mat_pers not like '%[^0-9]%' and len(q.mat_pers) between 1 and 18 then cast(q.mat_pers as bigint) end, q.mat_pers
                     offset :debut rows fetch next :taille rows only
                     """, nativeQuery = true)
       List<AffilPersProjection> pageAffilPers(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_nom_pers") String f_nom_pers,
                     @Param("f_pren_pers") String f_pren_pers,
                     @Param("f_etat_act") String f_etat_act,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("v_etat_act") int v_etat_act,
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
                       cin,
                       nom_pers,
                       pren_pers,
                       etat_act,
                       dat_emb,
                       cod_sit,
                       nbr_enf,
                       num_assur,
                       corps
                       from personnel
                       where cod_soc = :soc
                       and coalesce(typ_aff, 'S') = 'A'
                     ) q
                     where (:code is null or lower(ltrim(rtrim(q.mat_pers))) = :code)
                       and (:f_mat_pers is null or lower(ltrim(rtrim(q.mat_pers))) = :f_mat_pers)
                       and (:f_nom_pers is null or lower(cast(q.nom_pers as nvarchar(400))) like :f_nom_pers escape '!')
                       and (:f_pren_pers is null or lower(cast(q.pren_pers as nvarchar(400))) like :f_pren_pers escape '!')
                       and (:f_etat_act is null or lower(cast(q.etat_act as nvarchar(400))) like :f_etat_act escape '!')
                       and (:l1 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m1)
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l1 escape '!')
                            or (:v_etat_act = 1 and lower(cast(q.etat_act as nvarchar(400))) like :l1 escape '!'))
                       and (:l2 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m2)
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l2 escape '!')
                            or (:v_etat_act = 1 and lower(cast(q.etat_act as nvarchar(400))) like :l2 escape '!'))
                       and (:l3 is null
                            or (:v_mat_pers = 1 and lower(ltrim(rtrim(q.mat_pers))) = :m3)
                            or (:v_nom_pers = 1 and lower(cast(q.nom_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_pren_pers = 1 and lower(cast(q.pren_pers as nvarchar(400))) like :l3 escape '!')
                            or (:v_etat_act = 1 and lower(cast(q.etat_act as nvarchar(400))) like :l3 escape '!'))
                     """, nativeQuery = true)
       long compterAffilPers(@Param("soc") String soc,
                     @Param("code") String code,
                     @Param("f_mat_pers") String f_mat_pers,
                     @Param("f_nom_pers") String f_nom_pers,
                     @Param("f_pren_pers") String f_pren_pers,
                     @Param("f_etat_act") String f_etat_act,
                     @Param("v_mat_pers") int v_mat_pers,
                     @Param("v_nom_pers") int v_nom_pers,
                     @Param("v_pren_pers") int v_pren_pers,
                     @Param("v_etat_act") int v_etat_act,
                     @Param("m1") String m1,
                     @Param("l1") String l1,
                     @Param("m2") String m2,
                     @Param("l2") String l2,
                     @Param("m3") String m3,
                     @Param("l3") String l3);

}
