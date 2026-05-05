package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.gestion_adherent.entities.Corps;

public interface CorpsRepository extends JpaRepository<Corps, String> {
}
