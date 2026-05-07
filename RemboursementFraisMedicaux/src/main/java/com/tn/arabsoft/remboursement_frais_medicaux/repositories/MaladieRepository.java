package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.Maladie;

import java.util.List;

public interface MaladieRepository extends JpaRepository<Maladie, String> {

    @Query(value = "select * from maladie where apci = 'O'", nativeQuery = true)
    List<Maladie> getMaladie();
}
