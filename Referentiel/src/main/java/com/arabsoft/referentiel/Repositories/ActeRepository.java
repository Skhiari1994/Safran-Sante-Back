package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Acte;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActeRepository extends JpaRepository<Acte, String> {
}
