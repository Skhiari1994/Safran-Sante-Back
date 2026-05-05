package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.RegimeRemb;

import java.util.List;

public interface RegimeRembRepository extends JpaRepository<RegimeRemb, String> {

    @Query(value = "select * from regime_remb order by lib_remb", nativeQuery = true)
    List<RegimeRemb> getRegimeRemb();

}
