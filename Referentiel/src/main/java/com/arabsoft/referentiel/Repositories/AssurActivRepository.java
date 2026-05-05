package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.AssurActiv;
import com.arabsoft.referentiel.entities.cle.AssurActivCle;
import com.arabsoft.referentiel.projections.AssurActivProjection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@SuppressWarnings({ "java:S100", "java:S117" })
public interface AssurActivRepository extends JpaRepository<AssurActiv, AssurActivCle> {

  @Query(value = """
      select aa.cod_activite,
             aa.cod_assur,
             (select af.lib_activite
                from activite_famille af
               where af.cod_activite = aa.cod_activite) as libelle,
             aa.age_max,
             aa.age_alert
        from assur_activ aa
       where aa.cod_assur = :cod_assur
      """, nativeQuery = true)
  List<AssurActivProjection> GetListAssurActiv(@Param("cod_assur") String cod_assur);

}