package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.gestion_adherent.entities.Gouvernorat;

public interface GouvernoratRepository extends JpaRepository<Gouvernorat, String> {
}
