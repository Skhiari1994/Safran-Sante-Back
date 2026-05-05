package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.PlafondCnam;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePlafondAssur;

import java.util.List;

public interface PlafondCnamRepository extends JpaRepository<PlafondCnam, ClePlafondAssur> {

    @Query(value = "select * from plafond_cnam where mat_pers=:mat order by annee_cnam DESC", nativeQuery = true)
    List<PlafondCnam> getPlafondCnam(@Param("mat") String mat);
}
