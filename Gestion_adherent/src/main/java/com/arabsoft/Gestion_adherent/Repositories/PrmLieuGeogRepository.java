package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.gestion_adherent.entities.PrmLieuGeographique;

public interface PrmLieuGeogRepository extends JpaRepository<PrmLieuGeographique, String> {
}
