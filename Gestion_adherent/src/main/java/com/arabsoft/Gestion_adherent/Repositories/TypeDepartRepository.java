package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.arabsoft.gestion_adherent.entities.TypeDepart;

public interface TypeDepartRepository extends JpaRepository<TypeDepart, String> {
}
