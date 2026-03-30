package com.arabsoft.referentiel.Repositories;


import com.arabsoft.referentiel.Entities.AssurActiv;
import com.arabsoft.referentiel.Entities.Cle.AssurActivCle;
import com.arabsoft.referentiel.Projections.AssurActivProjection;
import com.arabsoft.referentiel.Projections.AssurFilProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssurActivRepository extends JpaRepository<AssurActiv, AssurActivCle>{

    @Query(value = "select aa.cod_activite,aa.cod_assur, \n" +
            "      (select af.lib_activite from activite_famille af where af.cod_activite = aa.cod_activite) as libelle,\n" +
            "       aa.age_max,aa.age_alert \n" +
            "from assur_activ aa where aa.cod_assur = :cod_assur", nativeQuery = true)
    List<AssurActivProjection> GetListAssurActiv(@Param("cod_assur") String cod_assur);

}