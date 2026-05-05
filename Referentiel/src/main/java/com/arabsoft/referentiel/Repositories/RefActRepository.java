package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.RefAct;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefActRepository extends JpaRepository<RefAct, String> {
}
