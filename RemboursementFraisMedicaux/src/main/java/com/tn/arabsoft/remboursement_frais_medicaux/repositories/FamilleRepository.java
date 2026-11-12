package com.tn.arabsoft.remboursement_frais_medicaux.repositories;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.Famille;
import com.tn.arabsoft.remboursement_frais_medicaux.entities.cle.CleFamille;

@Repository
public interface FamilleRepository extends JpaRepository<Famille, CleFamille> {

  @Query(value = """
          SELECT f.dat_naiss
          FROM famille f
          WHERE f.cod_soc = :codSoc
            AND f.mat_pers = :matPers
            AND f.num_fam = :numFam
            AND (
                  COALESCE(f.pec, 'N') = 'O'
                  OR COALESCE(f.pec_mut, 'N') = 'O'
                )
      """, nativeQuery = true)
  LocalDate findDatNais(
      @Param("codSoc") String codSoc,
      @Param("matPers") String matPers,
      @Param("numFam") Long numFam);

}
