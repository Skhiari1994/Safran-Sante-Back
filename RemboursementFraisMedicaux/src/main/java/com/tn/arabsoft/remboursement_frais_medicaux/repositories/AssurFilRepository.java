package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.AssurFil;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.AssurFilCle;

@Repository
public interface AssurFilRepository extends JpaRepository<AssurFil, AssurFilCle> {
}
