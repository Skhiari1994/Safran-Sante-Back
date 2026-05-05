package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.arabsoft.gestion_adherent.entities.Affectation;

@Repository
public interface AffectationRepository extends JpaRepository<Affectation, String> {
}
