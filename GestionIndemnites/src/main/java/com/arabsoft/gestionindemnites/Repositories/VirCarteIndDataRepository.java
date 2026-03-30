package com.arabsoft.gestionindemnites.Repositories;

import com.arabsoft.gestionindemnites.Entities.VirCarteIndData;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirCarteIndDataRepository extends JpaRepository<VirCarteIndData, Long> {
    List<VirCarteIndData> findBySeqOrderByLigne(Long seq);
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM VIR_CARTE_IND_DATA", nativeQuery = true)
    void deleteAllData();
}