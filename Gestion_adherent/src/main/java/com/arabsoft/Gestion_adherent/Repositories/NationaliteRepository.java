package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.gestion_adherent.entities.Nationalite;

public interface NationaliteRepository extends JpaRepository<Nationalite, String> {
}
