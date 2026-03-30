package com.arabsoft.referentiel.Repositories;

import com.arabsoft.referentiel.Entities.AssurFil;
import com.arabsoft.referentiel.Entities.Cle.AssurFilCle;
import com.arabsoft.referentiel.Projections.AssurFilProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository

public interface AssurFilRepository extends JpaRepository<AssurFil, AssurFilCle> {
    @Query(value = "SELECT a.cod_assur, " +
            "a.cod_fil, " +
            "a.mnt_adher, " +
            "a.mnt_enf, " +
            "a.mnt_conj, " +
            "a.mnt_pere, " +
            "a.mnt_mere, " +
            "a.prorat_pec, " +
            "(SELECT rf.lib_fill FROM ref_filliere rf WHERE cod_fil = a.cod_fil) AS libelle " +
            "FROM assur_fil a WHERE cod_assur = :cod_assur", nativeQuery = true)
    List<AssurFilProjection> GetListAssurFil(@Param("cod_assur") String cod_assur);
}
