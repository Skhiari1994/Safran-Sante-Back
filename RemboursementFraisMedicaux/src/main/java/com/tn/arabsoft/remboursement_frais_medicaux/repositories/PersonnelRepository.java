package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.Personnel;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePersonnel;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.*;

import java.util.List;

@SuppressWarnings({ "java:S117" })
public interface PersonnelRepository extends JpaRepository<Personnel, ClePersonnel> {

  @Query(value = """
      select
          p.num_retr,
          p.mat_pers,
          p.mat_int,
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
      """, nativeQuery = true)
  List<PersonnelBultSoinProjection> getPersonnelBultSoin(@Param("soc") String soc);

  @Query(value = """
      select num_fam, nom_pren
      from famille
      where cod_soc = :soc
        and mat_pers = :mat
        and coalesce(pec, 'N') = 'O'

      union

      select num_fam, nom_pren
      from famille
      where cod_soc = :soc
        and mat_pers = :mat
        and coalesce(pec_mut, 'N') = 'O'
        and nom_pren is not null
      """, nativeQuery = true)
  List<AdherentProjection> getListAdherent(@Param("soc") String soc, @Param("mat") String mat);

  @Query(value = """
      select num_fam, nom_pren
      from famille
      where cod_soc = :soc
        and mat_pers = :mat
        and coalesce(pec, 'N') = 'O'
      """, nativeQuery = true)
  List<AdherentProjection> getListAdherentDossMld(@Param("soc") String soc, @Param("mat") String mat);

  @Query(value = """
      select num_fam, nom_pren
      from famille
      where cod_soc = :soc
        and mat_pers = :mat
        and num_fam = :fam
        and coalesce(pec, 'N') = 'O'
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
      where p.etat_act = 'A'
      """, nativeQuery = true)
  List<PersonnelProjection> getPersActif();

  @Query(value = """
      select
          mat_pers,
          mat_int,
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
          mat_int,
          dat_nais,
          num_retr,
          concat(nom_pers, concat(' ', pren_pers)) as nom
      from personnel
      where cod_soc = :soc
        and etat_act = 'A' order by cast(mat_pers as int)
      """, nativeQuery = true)
  List<PersonnelProjection> getPersBultCnamFin(@Param("soc") String soc);

}