package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.VirFichData;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.VirFichDataId;

import java.util.List;

public interface VirFichDataRepository extends JpaRepository<VirFichData, VirFichDataId> {

    @Transactional(readOnly = true)
    @Query(value = "select * from vir_fich_data v where v.seq_ = :seq order by substr(v.ligne, -6)", nativeQuery = true)
    List<VirFichData> findByIdSeqOrderByLigne(Long seq);

    @Transactional(readOnly = true)
    @Query(value = "select count(*) from vir_fich_data v where v.seq_ = :seq", nativeQuery = true)
    Long countByIdSeq(Long seq);

}