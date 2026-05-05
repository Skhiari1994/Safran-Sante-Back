package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.arabsoft.gestion_adherent.entities.DiskPret;
import com.arabsoft.gestion_adherent.projections.DiskPretProjection;

import java.time.LocalDate;
import java.util.List;

public interface DiskPretRepository extends JpaRepository<DiskPret, Long> {

    @Query(value = """
            select *
            from disk_pret
            where dat_disk >= :startDate
              and dat_disk < :endDate
            """, nativeQuery = true)
    List<DiskPret> getDiskPret(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query(value = """
            select
                   t.dat_disk,
                   t.pret_disk,
                   t.num_retr,
                   t.mat_pers,
                   t.montant,
                   t.observation,
                   t.valid,
                   t.cod_grp_pret,
                   t.typ_pret,
                   t.cod_pret,
                   concat(p.nom_pers, concat(' ', p.pren_pers)) as nom
              from disk_pret t
              left join personnel p
                     on p.mat_pers = t.mat_pers
             where t.dat_disk >= :startDate
               and t.dat_disk < :endDate
            """, nativeQuery = true)
    List<DiskPretProjection> getDiskPretImport(
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}