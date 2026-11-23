package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.Corps;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CorpsRepository extends JpaRepository<Corps, String> {
}
