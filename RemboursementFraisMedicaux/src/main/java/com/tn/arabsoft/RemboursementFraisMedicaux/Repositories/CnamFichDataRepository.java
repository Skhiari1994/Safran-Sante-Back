package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;

import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.CnamFichData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CnamFichDataRepository extends JpaRepository<CnamFichData, CnamFichData.CnamFichDataId> {
    List<CnamFichData> findBySeqOrderByLigne(Long seq);
}
