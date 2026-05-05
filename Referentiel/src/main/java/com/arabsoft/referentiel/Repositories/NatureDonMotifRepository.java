package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.cle.CleNatureDonMotif;
import com.arabsoft.referentiel.entities.NatureDonMotif;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NatureDonMotifRepository extends JpaRepository<NatureDonMotif, CleNatureDonMotif> {

}
