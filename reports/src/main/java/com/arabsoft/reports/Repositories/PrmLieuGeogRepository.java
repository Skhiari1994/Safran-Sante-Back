package com.arabsoft.reports.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.reports.entities.PrmLieuGeographique;

public interface PrmLieuGeogRepository extends JpaRepository<PrmLieuGeographique, String> {
}
