package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.AssurActiv;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.AssurActivCle;

@Repository
public interface AssurActivRepository extends JpaRepository<AssurActiv, AssurActivCle> {

}