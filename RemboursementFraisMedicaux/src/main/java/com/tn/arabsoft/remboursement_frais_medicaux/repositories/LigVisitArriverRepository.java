package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.LigVisitArriver;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.LigVisitArriverCle;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.LigVisitArriverProjection;

import java.time.LocalDate;
import java.util.List;

@SuppressWarnings({ "java:S107" })
public interface LigVisitArriverRepository extends JpaRepository<LigVisitArriver, LigVisitArriverCle> {

  @Query(value = """
      select
          lbv.dat_act,
          lbv.abrv_act,
          a.lib_act,
          lbv.indice,
          lbv.mnt_honor,
          lbv.mnt_net,
          lbv.mnt_remb,
          case lbv.prf_typ
              when '1' then 'Personne physique'
              when '2' then 'Etablissement'
              else 'Autre'
          end as prf_type,
          lbv.prf_typ,
          concat(coalesce(re.etab_rsoc, ''), concat(' ', coalesce(re.pr_rsoc, ''))) as lib_org,
          lbv.prf_cod,
          lbv.dat_soin,
          lbv.cod_soc,
          lbv.num_fam,
          lbv.mat_pers,
          lbv.cod_visit,
          lbv.num_lig
      from lig_visit_arriver lbv
      left join acte a
             on a.abrv_act = lbv.abrv_act
      left join ref_etablis re
             on re.prf_cod = lbv.prf_cod
      where lbv.cod_soc = :cod_soc
        and lbv.mat_pers = :mat_pers
        and lbv.num_fam = :num_fam
        and lbv.dat_soin = :dat_soin
      """, nativeQuery = true)
  List<LigVisitArriverProjection> findLigVisitArriverById(
      @Param("cod_soc") String codSoc,
      @Param("mat_pers") String matPers,
      @Param("num_fam") Integer numFam,
      @Param("dat_soin") LocalDate datSoin);

  @Modifying
  @Transactional
  @Query(value = """
      delete from lig_visit_arriver
       where cod_soc = :soc
         and mat_pers = :mat
         and num_fam = :fam
         and dat_soin = :datSoin
         and abrv_act = :abrv
         and dat_act = :datAct
         and cod_visit = :codVisit
         and num_lig = :numLig
      """, nativeQuery = true)
  void deleteLigVisitArriver(
      @Param("soc") String soc,
      @Param("mat") String mat,
      @Param("fam") Integer fam,
      @Param("datSoin") LocalDate datSoin,
      @Param("abrv") String abrv,
      @Param("datAct") LocalDate datAct,
      @Param("codVisit") String codVisit,
      @Param("numLig") Long numLig);

  @Modifying
  @Transactional
  @Query(value = """
      delete from lig_visit_arriver
       where cod_soc = :codSoc
         and mat_pers = :matPers
         and num_fam = :numFam
         and dat_soin = :datSoin
      """, nativeQuery = true)
  void deleteByBulletin(
      @Param("codSoc") String codSoc,
      @Param("matPers") String matPers,
      @Param("numFam") Integer numFam,
      @Param("datSoin") LocalDate datSoin);

  @Query("""
      select max(l.num_lig)
        from LigVisitArriver l
       where l.cod_soc = :codSoc
         and l.mat_pers = :matPers
         and l.num_fam = :numFam
         and l.dat_soin = :datSoin
      """)
  Long findMaxNumLig(
      String codSoc,
      String matPers,
      Long numFam,
      LocalDate datSoin);

}