package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Maladie;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MaladieRepository extends JpaRepository<Maladie, String> {
}
