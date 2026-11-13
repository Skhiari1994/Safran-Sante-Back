package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.RefFiliere;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.FiliereProjection;

public interface RefFiliereRepository extends JpaRepository<RefFiliere, String> {

    // Code et libellé seulement : la liste ne dépend pas des autres colonnes de la table
    @Query(value = "select cod_fil, lib_fill from ref_filliere order by cod_fil", nativeQuery = true)
    List<FiliereProjection> getListFiliere();
}
