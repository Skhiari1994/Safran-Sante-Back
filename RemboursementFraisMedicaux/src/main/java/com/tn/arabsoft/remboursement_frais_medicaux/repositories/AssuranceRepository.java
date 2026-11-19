package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.Assurance;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.ListAssurProjection;

import java.util.List;

@Repository
public interface AssuranceRepository extends JpaRepository<Assurance, String> {

    @Query(value = "select cod_assur, lib_assur from assurance", nativeQuery = true)
    List<ListAssurProjection> getListAssurance();

    @Query(value = "select coalesce(duree_bult_mut, 0) from assurance where cod_assur = ?", nativeQuery = true)
    Integer getDureeBulletin(String codAssur);

}
