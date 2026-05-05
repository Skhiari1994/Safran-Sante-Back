package com.arabsoft.gestion_adherent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.arabsoft.gestion_adherent.entities.FichSal;

import java.util.List;

public interface FichSalRepository extends JpaRepository<FichSal, Long> {

    @Query(value = "select * from fich_sal order by ordre", nativeQuery = true)
    List<FichSal> getAllFichSal();

}
