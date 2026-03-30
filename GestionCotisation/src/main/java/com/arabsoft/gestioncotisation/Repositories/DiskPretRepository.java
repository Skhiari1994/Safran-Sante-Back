package com.arabsoft.gestioncotisation.Repositories;

import com.arabsoft.gestioncotisation.Entities.DiskPret;
import com.arabsoft.gestioncotisation.Projections.DiskPretProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiskPretRepository extends JpaRepository<DiskPret, Long> {


    @Query(value = " SELECT \n" +
            "            c.dat_disk AS dat_disk, \n" +
            "            c.pret_disk AS pret_disk, \n" +
            "            c.num_retr AS num_retr, \n" +
            "            c.mat_pers AS mat_pers, \n" +
            "            (SELECT p.nom_pers || ' ' || p.pren_pers \n" +
            "             FROM personnel p \n" +
            "             WHERE p.mat_pers = c.mat_pers) AS nom_pers, \n" +
            "            c.mat_pers AS mat_pers, \n" +
            "            c.montant AS montant, \n" +
            "            c.observation AS observation, \n" +
            "            c.valid AS valid, \n" +
            "            c.cod_grp_pret AS cod_grp_pret, \n" +
            "            c.typ_pret AS typ_pret, \n" +
            "            c.cod_pret AS cod_pret, \n" +
            "            c.id AS id\n" +
            "        FROM disk_pret c\n" +
            "        WHERE c.valid IN ('I', 'V')\n" +
            "        AND (:matPers IS NULL OR c.mat_pers = :matPers)\n" +
            "        AND (:corps IS NULL OR c.mat_pers IN (\n" +
            "            SELECT p.mat_pers FROM personnel p WHERE p.corps = :corps\n" +
            "        ))\n" +
            "        AND c.dat_disk >= TO_DATE(CONCAT('01/', :mois), 'DD/MM/YYYY')\n" +
            "        AND c.dat_disk < ADD_MONTHS(TO_DATE(CONCAT('01/', :mois), 'DD/MM/YYYY'), 1)\n" +
            "        AND c.pret_disk = '565'\n" +
            "        ORDER BY LPAD(c.num_retr, 10, '0')", nativeQuery = true)
    List<DiskPretProjection> findFilteredDiskPrets(
            @Param("matPers") String matPers,
            @Param("corps") String corps,
            @Param("mois") String mois);
}
