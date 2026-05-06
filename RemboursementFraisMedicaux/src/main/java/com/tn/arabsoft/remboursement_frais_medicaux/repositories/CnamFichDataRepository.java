package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.CnamFichData;

import java.util.List;

@Repository
public interface CnamFichDataRepository extends JpaRepository<CnamFichData, CnamFichData.CnamFichDataId> {

    List<CnamFichData> findBySeqOrderByLigne(Long seq);

}
