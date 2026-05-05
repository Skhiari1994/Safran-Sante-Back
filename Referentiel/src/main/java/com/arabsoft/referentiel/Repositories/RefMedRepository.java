package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.RefMed;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefMedRepository extends JpaRepository<RefMed, String> {
}
