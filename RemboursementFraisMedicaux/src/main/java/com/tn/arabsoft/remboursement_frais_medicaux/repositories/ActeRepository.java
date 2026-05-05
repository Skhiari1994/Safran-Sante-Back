package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.Acte;

public interface ActeRepository extends JpaRepository<Acte, String> {
}
