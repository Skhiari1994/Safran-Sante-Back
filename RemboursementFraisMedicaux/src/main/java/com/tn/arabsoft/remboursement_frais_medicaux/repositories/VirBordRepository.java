package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.VirBord;

import java.util.List;

public interface VirBordRepository extends JpaRepository<VirBord, String> {
    List<VirBord> findAllByOrderByOrdreAsc();
}
