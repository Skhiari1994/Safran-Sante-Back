package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.PrmLieuGeographique;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PrmLieuGeogRepository extends JpaRepository<PrmLieuGeographique, String> {
}
