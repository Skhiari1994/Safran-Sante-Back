package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.RefFiliere;

public interface RefFiliereRepository extends JpaRepository<RefFiliere, String> {
}