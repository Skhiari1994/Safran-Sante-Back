package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.CnamFileGen;

import java.util.Optional;

@Repository
public interface CnamFileGenRepository extends JpaRepository<CnamFileGen, Long> {
    Optional<CnamFileGen> findByCodSocAndCodBord(String cod_soc, String cod_bord);
}
