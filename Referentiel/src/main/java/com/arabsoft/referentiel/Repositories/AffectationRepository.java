package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AffectationRepository extends JpaRepository<Affectation, String> {
}
