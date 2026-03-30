package com.tn.arabsoft.RemboursementFraisMedicaux.Repositories;


import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.Cle.VirFichDataId;
import com.tn.arabsoft.RemboursementFraisMedicaux.Entities.VirFichData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface VirFichDataRepository extends JpaRepository<VirFichData, VirFichDataId> {
    @Transactional(readOnly = true)
    @Query(value = "SELECT * FROM VIR_FICH_DATA v WHERE v.seq_ = :seq ORDER BY SUBSTR(v.ligne, -6)", nativeQuery = true)
    List<VirFichData> findByIdSeqOrderByLigne(Long seq);

    @Transactional(readOnly = true)
    @Query(value = "SELECT COUNT(*) FROM VIR_FICH_DATA v WHERE v.seq_ = :seq", nativeQuery = true)
    Long countByIdSeq(Long seq);
}