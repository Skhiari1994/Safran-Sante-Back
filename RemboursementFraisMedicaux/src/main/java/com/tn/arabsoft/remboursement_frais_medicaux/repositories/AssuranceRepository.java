package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.Assurance;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.ListAssurProjection;

import java.util.List;

public interface AssuranceRepository extends JpaRepository<Assurance, String> {

    @Query(value = "select cod_assur, lib_assur from assurance", nativeQuery = true)
    List<ListAssurProjection> getListAssurance();

}
