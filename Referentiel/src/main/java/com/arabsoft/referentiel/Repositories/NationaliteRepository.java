package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Nationalite;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NationaliteRepository extends JpaRepository<Nationalite, String> {
}
