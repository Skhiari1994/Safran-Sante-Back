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
       order by mat_pers
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
       order by mat_pers
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
       order by mat_pers
      """, nativeQuery = true)
  AffilPersProjection getAffilPersByMat(@Param("soc") String soc,
      @Param("mat") String mat);

  @Query(value = """
      select *
        from affil_mutuelle
       where cod_soc = :soc
         and typ_aff = 'A'
         and etat_aff = 'I'
       order by mat_pers, dat_ass
      """, nativeQuery = true)
  List<AffilMutuelle> getAffilPersRenouvAdhesion(@Param("soc") String soc);

  @Query(value = """
      select t.cod_soc,
             (select concat(p.nom_pers, concat(' ', p.pren_pers))
                from personnel p
               where p.cod_soc = t.cod_soc
                 and p.mat_pers = t.mat_pers) as nompren,
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
       where t.cod_soc = :soc
         and coalesce(t.etat_aff, 'I') = 'I'
       order by t.mat_pers, t.dat_ass
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
      order by mat_pers, dat_ass
      """, nativeQuery = true)
  AffilMutuelle getAffilPersAffiliation(
      @Param("soc") String soc,
      @Param("mat") String mat,
      @Param("dat_ass") LocalDate dat_ass);

}
