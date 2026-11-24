package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.ParamOrganisme;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ParamOrganismeRepository extends JpaRepository<ParamOrganisme, Integer> {

    @Query("select coalesce(max(o.codOrg), 0) + 1 from ParamOrganisme o")
    Integer getNextId();

    /** Un libellé ne peut servir qu'à un seul organisme (sans tenir compte de la casse), hors l'organisme lui-même. */
    @Query("select count(o) > 0 from ParamOrganisme o where upper(o.libOrg) = upper(:libelle) and o.codOrg <> :idExclu")
    boolean existeLibelle(@Param("libelle") String libelle, @Param("idExclu") Integer idExclu);

}
