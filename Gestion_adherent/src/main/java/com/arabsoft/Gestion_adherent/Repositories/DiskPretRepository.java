package com.arabsoft.Gestion_adherent.Repositories;

import com.arabsoft.Gestion_adherent.Entities.DiskPret;
import com.arabsoft.Gestion_adherent.Projections.DiskPretProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DiskPretRepository extends JpaRepository<DiskPret,Long> {

    @Query(value="select * from disk_pret where  to_char(dat_disk,'mm/yyyy')=to_char(:mois,'mm/yyyy')",nativeQuery = true)
    List<DiskPret> getDiskPret(@Param("mois") String mois);



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
        (
            SELECT p.nom_pers || ' ' || p.pren_pers
            FROM personnel p
            WHERE p.mat_pers = t.mat_pers
        ) AS nom
    FROM disk_pret t
    WHERE TO_CHAR(t.dat_disk, 'MM/YYYY') = :mois
    
    """, nativeQuery = true)

    List<DiskPretProjection> getDiskPretImport(@Param("mois") String mois);


}
