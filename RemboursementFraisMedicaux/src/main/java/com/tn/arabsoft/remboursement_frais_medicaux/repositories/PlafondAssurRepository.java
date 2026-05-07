package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.tn.arabsoft.remboursement_frais_medicaux.entities.PlafondAssur;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.ClePlafondAssur;
import com.tn.arabsoft.remboursement_frais_medicaux.projections.PlafondAssurProjection;

import java.util.List;

public interface PlafondAssurRepository extends JpaRepository<PlafondAssur, ClePlafondAssur> {

    @Query(value = """
            select
                t.annee_assur,
                t.cod_soc,
                t.mat_pers,
                t.cod_assur,
                t.plafond,
                t.sold_plaf,
                t.sold_assur_estim,
                a.lib_assur as libAssur
            from plafond_assur t
            left join assurance a
                   on a.cod_assur = t.cod_assur
            where t.mat_pers = :mat
            order by t.annee_assur desc
            """, nativeQuery = true)
    List<PlafondAssurProjection> getPlafondAssur(@Param("mat") String mat);

}