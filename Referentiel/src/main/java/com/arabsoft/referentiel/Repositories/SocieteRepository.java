package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Societe;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SocieteRepository extends JpaRepository<Societe, String> {
}
