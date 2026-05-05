package com.arabsoft.referentiel.repositories;

import com.arabsoft.referentiel.entities.NatureDon;
import com.arabsoft.referentiel.projections.AffectationProjection;
import com.arabsoft.referentiel.projections.CompteIndParamProjection;
import com.arabsoft.referentiel.projections.NatureDonProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NatureDonRepository extends JpaRepository<NatureDon, String> {

        @Query(value = """
                        select c.num_cpt,
                               c.lib_cpt
                          from compte c
                         where length(num_cpt) > 3
                           and num_cpt like '6%'
                        """, nativeQuery = true)
        List<CompteIndParamProjection> getListCompte();

        @Query(value = """
                        select cod_affect,
                               lib_affect,
                               lib_affect_a
                          from affectation
                         order by cod_affect
                        """, nativeQuery = true)
        List<AffectationProjection> getAffectation();

        @Query(value = """
                        select n.nat_don,
                               n.cod_affect,
                               a.lib_affect,
                               a.lib_affect_a
                          from nature_don_motif n
                          join affectation a
                            on n.cod_affect = a.cod_affect
                         where n.nat_don = :nat
                        """, nativeQuery = true)
        List<NatureDonProjection> getAffectationByNat(@Param("nat") String nat);

}
