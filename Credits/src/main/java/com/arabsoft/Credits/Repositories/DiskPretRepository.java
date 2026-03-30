package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.DiskPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiskPretRepository extends JpaRepository<DiskPret,Long> {

    @Query(value = """
    SELECT
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
        t.id,
        (SELECT p.nom_pers || ' ' || p.pren_pers
           FROM personnel p
          WHERE p.mat_pers = t.mat_pers
        ) AS nom
    FROM disk_pret t
    WHERE t.valid IN ('I','V','E')
      AND t.mat_pers = NVL(:mat, t.mat_pers)
      AND t.mat_pers IN (
            SELECT p.mat_pers
            FROM personnel p
            WHERE p.corps = NVL(:corps, p.corps)
      )
      AND t.pret_disk IN (
            SELECT DISTINCT tp.abrv_fixe
            FROM type_pret tp
      )
      AND TO_CHAR(t.dat_disk, 'MM/YYYY') = :mois
      AND t.pret_disk = NVL(:codGrp, t.pret_disk)
    """,
            nativeQuery = true)
    List<DiskPret> getDiskPret(
            @Param("mat") String mat,
            @Param("corps") String corps,
            @Param("mois") String mois,
            @Param("codGrp") String codGrp
    );

}
