package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.gestion_adherent.entities.Banque;

public interface BanqueRepository extends JpaRepository<Banque, String> {
}
