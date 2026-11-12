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
            select t.annee_assur,
                   t.cod_soc,
                   t.mat_pers,
                   t.cod_assur,
                   t.plafond,
                   t.sold_plaf,
                   t.sold_assur_estim,
                   t.num_fam,
                   case
                       when t.num_fam = 0 then 'Adhérent'
                       else f.nom_pren
                   end as nom_prest,
                   a.lib_assur as libAssur
            from plafond_assur t
            left join assurance a
                   on a.cod_assur = t.cod_assur
            left join famille f
                   on t.cod_soc = f.cod_soc
                  and t.mat_pers = f.mat_pers
                  and t.num_fam = f.num_fam
            where t.cod_soc = :soc
              and t.mat_pers = :mat
            order by t.annee_assur desc,
                     t.cod_soc,
                     t.mat_pers,
                     t.num_fam
            """, nativeQuery = true)
    List<PlafondAssurProjection> getPlafondAssur(
            @Param("soc") String soc,
            @Param("mat") String mat);

}