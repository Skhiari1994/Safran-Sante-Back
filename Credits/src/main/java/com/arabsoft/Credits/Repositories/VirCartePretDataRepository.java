package com.arabsoft.Credits.Repositories;

import com.arabsoft.Credits.Entities.VirCartePretData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VirCartePretDataRepository extends JpaRepository<VirCartePretData, Long> {
    List<VirCartePretData> findBySeqOrderByLigne(Long seq);
}