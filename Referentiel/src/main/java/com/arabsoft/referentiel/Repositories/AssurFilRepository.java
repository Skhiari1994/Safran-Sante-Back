package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.AssurFil;
import com.arabsoft.referentiel.entities.cle.AssurFilCle;
import com.arabsoft.referentiel.projections.AssurFilProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings({ "java:S100", "java:S117" })
public interface AssurFilRepository extends JpaRepository<AssurFil, AssurFilCle> {
    @Query(value = """
            select a.cod_assur,
                   a.cod_fil,
                   a.mnt_adher,
                   a.mnt_enf,
                   a.mnt_conj,
                   a.mnt_pere,
                   a.mnt_mere,
                   a.prorat_pec,
                   (select rf.lib_fill
                      from ref_filliere rf
                     where rf.cod_fil = a.cod_fil) as libelle
              from assur_fil a
             where a.cod_assur = :cod_assur
            """, nativeQuery = true)
    List<AssurFilProjection> GetListAssurFil(@Param("cod_assur") String cod_assur);
    
}
