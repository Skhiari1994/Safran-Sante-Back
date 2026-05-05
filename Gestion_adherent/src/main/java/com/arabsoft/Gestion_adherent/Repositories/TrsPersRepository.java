package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.gestion_adherent.entities.TrsPers;

public interface TrsPersRepository extends JpaRepository<TrsPers, String> {
}
